/***************************************************************************
                          operations.h  -  implementation of the math stuff
                             -------------------
    begin                : Wed Mar 23 2005
    copyright            : (C) 2005 by Ricardo Cruz
    email                : rick2@aeiou.pt
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

#ifndef OPERATIONS_H
#define OPERATIONS_H

typedef enum { POSITIVE=1, NEGATIVE=-1 } SIGNAL;

typedef enum { FALSE, TRUE } BOOL;

// we work with fractions internally
typedef struct {
  SIGNAL signal;
  unsigned int num;
  unsigned int den;
  BOOL error;
} Frac;

void ini_frac(Frac*);
void copy_frac(const Frac* src, Frac* dst);
void print_frac(const char* etiqueta, const Frac*);

void toggle_signal(Frac* f);
void simplify(Frac* f);
void sum(const Frac* f1, const Frac* f2, Frac* res);
void subtract(const Frac* f1, const Frac* f2, Frac* res);
void multiply(const Frac* f1, const Frac* f2, Frac* res);
void divide(const Frac* f1, const Frac* f2, Frac* res);
void power(const Frac* f1, const Frac* f2, Frac* res);

#endif /*OPERATIONS_H*/
