#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <curses.h>
#include <time.h>
#include "gamelib.h"
#include "turtle.c"
#include "text.h"

static const char *device = "/dev/fb1";

struct rectangle{
    int x;
    int y;
    int oldx;
    int oldy;
    int width;
    int height;
    int xspeed;
    int yspeed;
    struct rectangle* next;
};

enum Game_State{
    start,
    play,
    paused,
    game_over
};

void grow(struct rectangle* head);

// Initializes all the pixels of a frame to the same color
void initFrame(uint16_t frame[GRAPHICS_HEIGHT][GRAPHICS_WIDTH], uint16_t color)
{
    //Don't need this because I'm drawing a picture for my background which acts the same way initFrame would
}

// Draws a rectangle on the screen, checking for edge cases where the rectangle could be partially off screen.
void drawRect(uint16_t frame[GRAPHICS_HEIGHT][GRAPHICS_WIDTH], int x, int y, int height, int width, uint16_t color)
{
    int row, col;
    for (row = y; row < y + height; row++){
        for (col = x; col < x + width; col++){
            if (row > 0 && row < GRAPHICS_HEIGHT && col > 0 && col < GRAPHICS_WIDTH){
                frame[row][col] = color;
            }
        }
    }

}

// Draws an image to the screen. Note that the frame stores uint16_t, while the pixels are given in uint8_t.
void drawImage(uint16_t frame[GRAPHICS_HEIGHT][GRAPHICS_WIDTH], int x, int y, int height, int width, const uint8_t *pixels)
{
    uint16_t *p = (uint16_t*)pixels;
    int row, col;
    for (row = y; row < height; row++){
	for (col = x; col < width; col++){
	    frame[row][col] = *(p+ (row*width + col));
	}
    }
}

// Draws a single character to the screen.
// HINT: look at text.h. charHasPixelSet will tell you if any given character has a pixel set at coordinates i, j.
// You can find fontsize_t in gamelib.h. It is a simple scaling factor, so if a character has a single pixel set and the font
// size is medium, you should fill a 3x3 area on the screen with color. If the font is huge, you should fill a 5x5 area with color.
void drawChar(uint16_t frame[GRAPHICS_HEIGHT][GRAPHICS_WIDTH], int x, int y, char c, uint16_t color, fontsize_t fontsize)
{
    //there HAS to be a better way to do this...
    int i, j;
    for (i = y; i < y + 13; i++){
	for (j = x; j < x + 7; j++){
	    if (charHasPixelSet(c, i-y, j-x)){
		int a, b;
		for (a = 0; a < fontsize; a++){
		    for (b = 0; b <fontsize; b++){
			 frame[i*fontsize+a][j*fontsize+b] = color;
		    }
		}
	    }
	}
    }
}

// Draws a string to the screen, using drawChar. Make sure you account for font size in your character spacing.
void drawString(uint16_t frame[GRAPHICS_HEIGHT][GRAPHICS_WIDTH], int x, int y, char *string, uint16_t color, fontsize_t fontsize)
{
    int i = 0;
    while (*(string+i) != 0x0A){
	drawChar(frame, x+(7*fontsize*i), y, *(string+i), color, fontsize);
	i++;
    }
}

void moveRect(struct rectangle* r){
    if ((r->x + r->width) > GRAPHICS_WIDTH || (r->x < 0)){
	 r->xspeed = (-1)*r->xspeed;
    }
    if ((r->y + r->height) > GRAPHICS_HEIGHT || (r->y < 0)){
	r->yspeed = (-1)*r->yspeed;
    }

    r->oldx = r->x;
    r->oldy = r->y;
    r->x += r->xspeed;
    r->y += r->yspeed;
}

int detectRect(struct rectangle* r1, struct rectangle* r2){
    if (r1->x + (r1->width)/2 > r2->x && r1->x + (r1->width)/2 < r2->x + r2->width && r1->y + (r1->height)/2 > r2->y && r1->y + (r1->height)/2 < r2->y + r2->height){
	return 1;
    }
    else return 0;
}

int detectTail(struct rectangle* head){
    struct rectangle* curr = head;

    while (curr->next != NULL){
	if (detectRect(head, curr->next)){
            return 1;
	}

	curr = curr->next;
    }

    return 0;
}

void moveFood(struct rectangle* food){
    //Not fully random because I'm not seeding it, but good enough for my purposes
    food->x = rand()%(GRAPHICS_WIDTH - food->width);
    food->y = rand()%(GRAPHICS_HEIGHT - food->height);
}

void eatFood(struct rectangle* r1, struct rectangle* food, int* score){
    if (detectRect(r1, food)){
	moveFood(food);

	printf("Eating Food\n\r");
	refresh();

	*score = *score + 1;

	printf("Score:%d\n\r",*score);
	refresh();

	grow(r1);
    }
}

void grow(struct rectangle* head){
    struct rectangle* curr = head;

    while (curr->next != NULL){
	curr = curr->next;
    }

    curr->next = malloc(sizeof(struct rectangle));

    curr->next->x = -20;
    curr->next->y = -20;
    curr->next->width = curr->width;
    curr->next->height = curr->height;
    curr->next->xspeed = curr->xspeed;
    curr->next->yspeed = curr->yspeed;
    curr->next->next = NULL;
}

void updateSnake(struct rectangle* head){
    struct rectangle* curr = head;

    while (curr->next != NULL){

	curr->next->oldx = curr->next->x;
	curr->next->oldy = curr->next->y;
	curr->next->x = curr->oldx;
	curr->next->y = curr->oldy;
	curr->next->xspeed = curr->xspeed;
	curr->next->yspeed = curr->yspeed;
	curr = curr->next;
    }
}

void drawSnake(uint16_t frame[GRAPHICS_HEIGHT][GRAPHICS_WIDTH], struct rectangle* head, uint16_t color){
    struct rectangle* curr = head;

    while (curr->next != NULL){
	drawRect(frame, curr->next->x, curr->next->y, curr->next->width, curr->next->height, color);
	curr = curr->next;
    }
}

void resetSnake(struct rectangle* head, int* score){
    struct rectangle* curr = head->next;
    struct rectangle* prev;

    while (curr != NULL){
	prev = curr;
	free(prev);
	curr = curr->next;
    }

    head->next = NULL;

    *score = 0;
}

int main(int argc, char **argv)
{
    // Initialize graphics
    int fd = open(device, O_RDWR);
    uint16_t frame[GRAPHICS_HEIGHT][GRAPHICS_WIDTH];
    enableInput();
    initTimer(20.0);

    //My rects
    struct rectangle* r1 = malloc(sizeof(struct rectangle));
    r1->x=20;
    r1->y=20;
    r1->height=10;
    r1->width=10;
    r1->xspeed=6;
    r1->yspeed=0;
    r1->next=NULL;

    struct rectangle* food = malloc(sizeof(struct rectangle));
    food->height = 14;
    food->width = 14;
    food->xspeed = 0;
    food->yspeed = 0;
    food->x = rand()%(GRAPHICS_WIDTH - food->width);
    food->y = rand()%(GRAPHICS_HEIGHT - food->height);

    //Game State
    enum Game_State state = start;

    //Score
    int* counter = malloc(sizeof(int));
    *counter = 0;

    // Main loop
    while(1) {

        waitTimer();
	drawImage(frame, 0, 0, turtle.height, turtle.width, turtle.pixel_data);

	//should have done a switch for gamestate but oh well
	if (state == start){
	    if (getKeyPress() == 0x20){
		state = play;
	    }

	    drawString(frame, 5, 5, "TURTL\n", 0x408F, MEDIUM);
	    drawString(frame, 5, 25, "Press space\n", 0xFFFF, SMALL);
	    drawString(frame, 5, 40, "to begin\n", 0xFFFF, SMALL);
	    drawString(frame, 5, 55, "or pause!\n", 0xFFFF, SMALL);
	}
	else if(state == play){
	    switch (getKeyPress()){
	        case KEY_UP:
		    r1->yspeed = -6;
		    r1->xspeed = 0;
		    break;
	        case KEY_DOWN:
		    r1->yspeed = 6;
		    r1->xspeed = 0;
		    break;
	        case KEY_LEFT:
		    r1->xspeed = -6;
		    r1->yspeed = 0;
		    break;
	        case KEY_RIGHT:
		    r1->xspeed = 6;
		    r1->yspeed = 0;
		    break;
		case 0x20:
		    state = paused;
		    break;
	    }

	    moveRect(r1);
	    updateSnake(r1);
	    eatFood(r1, food, counter);
	    drawRect(frame, r1->x, r1->y, r1->width, r1->height, 0x408F);
	    drawSnake(frame, r1, 0x408F);
	    drawRect(frame, food->x, food->y, food->width, food->height, 0xA00F);

	    drawString(frame,2,2,"TURTL\n", 0xFFFF,SMALL);

	    if (detectTail(r1)){
		printf("Colliding with tail\n\r");
		refresh();

		state = game_over;
	    }
	}
	else if (state == paused){
	    if (getKeyPress() == 0x20){
		state = play;
	    }

	    drawRect(frame, r1->x, r1->y, r1->width, r1->height, 0x408F);
            drawSnake(frame, r1, 0x408F);
            drawRect(frame, food->x, food->y, food->width, food->height, 0xA00F);

	    drawString(frame, 5, 5, "PAUSE\n", 0x408F, MEDIUM);
	    drawString(frame, 5, 25, "Press space\n", 0xFFFF, SMALL);
            drawString(frame, 5, 40, "to continue\n", 0xFFFF, SMALL);
	}
	else if (state == game_over){
	    if (getKeyPress() == 0x20){
		state = play;
	    }

	    resetSnake(r1, counter);

	    drawString(frame, 5, 5, "GAME\n", 0x408F, MEDIUM);
	    drawString(frame, 5, 25, "OVER\n", 0x408F, MEDIUM);
	    drawString(frame, 5, 65, "Press space\n", 0xFFFF, SMALL);
            drawString(frame, 5, 80, "to retry!\n", 0xFFFF, SMALL);
	}

        outputFrame(fd, frame);
    }

    stopTimer();
    disableInput();
    close(fd);
    return 0;
}
