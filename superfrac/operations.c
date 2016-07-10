/***************************************************************************
                          operations.c  -  implementation of the math stuff
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

#include <stdio.h>

#include "operations.h"

// math operations implemented in the end of file
int gcd(int a, int b);
int elevar(int base, int exp);
void swap(int* a, int* b);
inline BOOL is_base_10(int n);

/* Operations envolving our Frac structure. */

void ini_frac(Frac* f)
  {
  f->num = (f->den = 0);
  f->signal = POSITIVE;
  f->error = FALSE;
  }

void copy_frac(const Frac* src, Frac* dst)
  {
  dst->num = src->num;
  dst->den = src->den;
  dst->signal = src->signal;
  dst->error  = src->error;
  }

void print_frac(const char* lbl, const Frac* f)
  {
  if(f->den == 1)  // print integer
    printf("%s= %c%d\n", lbl, (f->signal == NEGATIVE) ? '-' : ' ', f->num);
  else if(is_base_10(f->den))
    {  // fraccaionary of base 10, print as racional
    printf("%s= %c", lbl, (f->signal == NEGATIVE) ? '-' : ' ');

    int num = f->num, den = f->den;
    if(num*10 > den)
      printf("%d", num/den);
    else
      putchar('0');
    putchar('.');

    for(den/=10; den > 0; den /= 10)
      putchar((num/den) - (num/(den*10)*10) + '0');
    putchar('\n');
    }
  else  // fraccionary
    printf("%s= %c%d/%d\n", lbl, (f->signal == NEGATIVE) ? '-' : ' ', f->num, f->den);
  }

void toggle_signal(Frac* f)
  {
  if(f->signal == POSITIVE)
    f->signal = NEGATIVE;
  else// if(f->signal == NEGATIVE)
    f->signal = POSITIVE;
  }

void simplify(Frac* f)
  {
  int mdc = gcd(f->num, f->den);
  f->num /= mdc;
  f->den /= mdc;
  }

/* If you had more operations, don't forget modifying operation_priority at
   superfrac.c */

void sum(const Frac* f1, const Frac* f2, Frac* res)
  {
  int num = (f1->num * f2->den * f1->signal) +
            (f2->num * f1->den * f2->signal);
  if(num < 0)
    {
    res->signal = NEGATIVE;
    res->num   = -num;
    }
  else
    {
    res->signal = POSITIVE;
    res->num = num;
    }
  res->den = f1->den * f2->den;
  simplify(res);
  res->error = FALSE;
  }

void subtract(const Frac* f1, const Frac* f2, Frac* res)
  {
  int num = (f1->num * f2->den * f1->signal) +
             (f2->num * f1->den * (-f2->signal));
  if(num < 0)
    {
    res->signal = NEGATIVE;
    res->num    = -num;
    }
  else
    {
    res->signal = POSITIVE;
    res->num    = num;
    }
  res->den = f1->den * f2->den;
  simplify(res);
  res->error = FALSE;
  }

void multiply(const Frac* f1, const Frac* f2, Frac* res)
  {
  res->num = f1->num * f2->num;
  res->den = f1->den * f2->den;
  simplify(res);
  res->signal = (f1->signal == f2->signal) ? POSITIVE : NEGATIVE;
  res->error = FALSE;
  }

void divide(const Frac* f1, const Frac* f2, Frac* res)
  {
  unsigned int num = f1->num * f2->den;
  unsigned int den = f1->den * f2->num;

  res->num = num;
  res->den = den;
  simplify(res);

  res->signal = (f1->signal == f2->signal) ? POSITIVE : NEGATIVE;
  if(res->den == 0)
    res->error = TRUE;
  else
    res->error = FALSE;
  }

void power(const Frac* f1, const Frac* f2, Frac* res)
  {
  unsigned int num = f1->num;
  unsigned int den = f1->den;
  if(f2->signal == NEGATIVE)
    swap(&num, &den);
  num = elevar(num, f2->num);
  den = elevar(den, f2->num);
  res->num = num;
  res->den = den;
  if(f1->signal == NEGATIVE && f2->num % 2 != 0)
    res->signal = NEGATIVE;
  if(f2->den != 1)
    res->error = TRUE;
  else
    res->error = FALSE;
  }

/* Math operations. */
// we could use math library for this, but we dont use that much, so...

/* Calculates the Maximum Common Divider by the Euclides algorithm. */
int gcd(int a, int b)
  {
  if(a == b)
    return a;

  // according to Euclides theorem: MDC(a,b) = MDC(b,r)
  int aux;
  while(b != 0)
    {
    aux = a;
    a = b;
    b = aux % a;
    }

  return a;
  }

// expoent must be positive!
int elevar(int base, int exp)
  {
#if 0
  if(exp < 0)
    {
    printf("Internal error: expoent must have a negative value.\n");
    return 0;
    }
#endif
  if(exp == 0)
    return 1;
  int b = base;
  for(; exp > 1; exp--)
    base *= b;
  return base;
  }

void swap(int* a, int* b)
  {
  int t = *b;
  *b = *a;
  *a = t;
  }

inline BOOL is_base_10(int n)
  {
  for(; n > 1; n/=10)
    if(n % 10 != 0)
      return FALSE;
  return TRUE;
  }
