/**
 *
 */
package org.libertybikes.game.bot;

import java.util.Random;

import org.libertybikes.game.core.DIRECTION;
import org.libertybikes.game.core.GameBoard;

public class Hal extends AIPlayer {

    private int ticksTillRandomMove = 20;
    private int ticksTillMove = 4;
    private int numOfRandomMoves = 0;
    static Random ran = new Random();
    // Collision detection distance
    private final static int CD = 1;
    // Border detection distance
    private final static int BD = 2;

    private DIRECTION direction, lastDirection, lastLastDirection;

    private int x, y;
    private boolean hasMoved = false;

    public Hal(int startX, int startY, int width, int height, DIRECTION startDirection, short takenSpotNumber) {
        super(startX, startY, width, height, startDirection, takenSpotNumber);
        lastLastDirection = lastDirection = direction = startDirection;
        x = startX;
        y = startY;
    }

    @Override
    public DIRECTION processGameTick(short[][] board) {
        if (hasMoved) {
            switch (lastDirection) {
                case DOWN:
                    y++;
                    break;
                case LEFT:
                    x--;
                    break;
                case RIGHT:
                    x++;
                    break;
                case UP:
                    y--;
                    break;
            }
        }

        //System.out.println("x: " + x + " y: " + y + " lastDirection: " + lastDirection + " direction: " + direction);

        hasMoved = true;

        if (--ticksTillRandomMove < 1 && numOfRandomMoves < 9) {
            direction = setDirection(DIRECTION.values()[ran.nextInt(4)]);
            ticksTillRandomMove = 35;
            numOfRandomMoves++;

        } else {

            if (--ticksTillMove > 1) {
                return direction;
            }

            ticksTillMove = 1;

            switch (direction) {
                case DOWN:
                    if (ran.nextBoolean()) {
                        if (y + height + BD > GameBoard.BOARD_SIZE || checkCollision(board, x, y + CD)) {
                            if (checkCollision(board, x + CD, y)) {
                                direction = DIRECTION.LEFT;
                            }
                            direction = DIRECTION.RIGHT;
                        }
                    } else {
                        if (y + height + BD > GameBoard.BOARD_SIZE || checkCollision(board, x, y + CD)) {
                            if (checkCollision(board, x - CD, y)) {
                                direction = DIRECTION.RIGHT;
                            }
                            direction = DIRECTION.LEFT;
                        }
                    }
                    break;
                case LEFT:
                    if (ran.nextBoolean()) {
                        if (x - BD < 0 || checkCollision(board, x - CD, y)) {
                            if (checkCollision(board, x, y - CD)) {
                                direction = DIRECTION.DOWN;
                            }
                            direction = DIRECTION.UP;
                        }
                    } else {
                        if (x - BD < 0 || checkCollision(board, x - CD, y)) {
                            if (checkCollision(board, x, y + CD)) {
                                direction = DIRECTION.UP;
                            }
                            direction = DIRECTION.DOWN;
                        }
                    }
                    break;
                case RIGHT:
                    if (ran.nextBoolean()) {
                        if (x + width + BD > GameBoard.BOARD_SIZE || checkCollision(board, x + CD, y)) {
                            if (checkCollision(board, x, y - CD)) {
                                direction = DIRECTION.DOWN;
                            }
                            direction = DIRECTION.UP;
                        }
                    } else {
                        if (x + width + BD > GameBoard.BOARD_SIZE || checkCollision(board, x + CD, y)) {
                            if (checkCollision(board, x, y + CD)) {
                                direction = DIRECTION.UP;
                            }
                            direction = DIRECTION.DOWN;
                        }
                    }
                    break;
                case UP:
                    if (ran.nextBoolean()) {
                        if (y - BD < 0 || checkCollision(board, x, y - CD)) {
                            if (checkCollision(board, x + CD, y)) {
                                direction = DIRECTION.LEFT;
                            }
                            direction = DIRECTION.RIGHT;
                        }
                    } else {
                        if (y - BD < 0 || checkCollision(board, x, y - CD)) {
                            if (checkCollision(board, x - CD, y)) {
                                direction = DIRECTION.RIGHT;
                            }
                            direction = DIRECTION.LEFT;
                        }
                    }
                    break;
            }
        }

        switch (direction) {
            case DOWN:
                if (y + height + BD > GameBoard.BOARD_SIZE || checkCollision(board, x, y + CD)) {
                    direction = setDirection(DIRECTION.UP);
                }
                break;
            case LEFT:
                if (x - BD < 0 || checkCollision(board, x - CD, y)) {
                    direction = setDirection(DIRECTION.RIGHT);
                }
                break;
            case RIGHT:
                if (x + width + BD > GameBoard.BOARD_SIZE || checkCollision(board, x + CD, y)) {
                    direction = setDirection(DIRECTION.LEFT);
                }
                break;
            case UP:
                if (y - BD < 0 || checkCollision(board, x, y - CD)) {
                    direction = setDirection(DIRECTION.DOWN);
                }
                break;
        }
        lastLastDirection = lastDirection;
        lastDirection = direction;

        direction = dontBackOntoSelf();
        return direction;
    }

    private boolean checkCollision(short[][] board, int x, int y) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[x + i][y + j] == GameBoard.PLAYER_SPOT_TAKEN + takenSpotNumber || board[x + i][y + j] == GameBoard.SPOT_AVAILABLE) {
                } else {
                    //System.out.println("Collision DETECTED: " + direction + " at [" + (x + i) + "][" + (y + j) + "]");
                    return true;
                }
            }
        }
        return false;
    }

    private DIRECTION setDirection(DIRECTION newDirection) {

        if ((newDirection == DIRECTION.UP && lastDirection == DIRECTION.DOWN) ||
            (newDirection == DIRECTION.DOWN && lastDirection == DIRECTION.UP) ||
            (newDirection == DIRECTION.LEFT && lastDirection == DIRECTION.RIGHT) ||
            (newDirection == DIRECTION.RIGHT && lastDirection == DIRECTION.LEFT)) {
            return lastDirection;
        }

        return newDirection;
    }

    private DIRECTION dontBackOntoSelf() {
        switch (direction) {
            case DOWN:
                if (lastLastDirection == DIRECTION.UP)
                    return DIRECTION.UP;
                break;
            case LEFT:
                if (lastLastDirection == DIRECTION.RIGHT)
                    return DIRECTION.RIGHT;
                break;
            case RIGHT:
                if (lastLastDirection == DIRECTION.LEFT)
                    return DIRECTION.LEFT;
                break;
            case UP:
                if (lastLastDirection == DIRECTION.DOWN)
                    return DIRECTION.DOWN;
                break;
        }
        return direction;
    }

}