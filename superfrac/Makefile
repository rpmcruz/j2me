# SuperFrac Makefile
# type 'make' to compile
# 'make clean' to remove compilation results (ie. for packaging)

# Readline has the disavantage of being a requirment, but with
# the advantage of providing History and some bindings support to
# our input.
# You may disable it, by deleting ENABLED .

READLINE = ENABLED
CC = gcc
CFLAGS = -Wall -DREADLINE_$(READLINE)
ifeq ($(READLINE), ENABLED)
  LIBS = -lreadline -lcurses
else
  LIBS =
endif
SRC = superfrac.c operations.c
OBJ = $(SRC:.c=.o)
TARGET = superfrac

all: $(TARGET)

$(TARGET): $(OBJ)
	$(CC) $(LIBS) -o $(TARGET) $(OBJ)

clean:
	rm -f $(TARGET) $(OBJ) core*
