/***************************************************************************
                          superfrac.c  -  parser and processor
                             -------------------
    begin                : Fri Mar 18 2005
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
#include <stdlib.h>

#include "operations.h"

#define SYNTAX_ERROR(c,s) { printf("Syntax error: %s\n" \
                            "Character not expected: '%c'.\n", s,c); \
                            force_cleanup(first_parcel); return; }
#define CALCULUS_ERROR(c,s) { printf("Calculus error on %c.\n" \
                              "Probable cause: %s\n", c,s); \
                              force_cleanup(first_parcel); return; }
#define INSTRUCTION_ERROR(s,c) { printf("Instruction error: %s%c.\n", s, c); \
                                 force_cleanup(first_parcel); return; }

inline BOOL is_number(char c)
  { return c >= '0' && c <= '9'; }
inline BOOL is_letter(char c)
  { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'); }
inline int convert_index(char c)
  { if(c >= 'A' && c <= 'Z') return c - 'A'; return c - 'a'; }

void remove_spaces(char str[]);

#define MAX_PRIORITY 2
inline BOOL is_operation(char c)
  { return c == '^' || c == '*' || c == '/' || c == '+' || c == '-'; }
static const char operation_priority[][2] = {
  { '^', 2 },
  { '*', 1 },
  { '/', 1 },
  { '+', 0 },
  { '-', 0 },
  { ' ', 0 }
  };

char get_priority(char op)
  {
  int i;
  for(i = 0; operation_priority[i][0] != ' '; i++)
    if(operation_priority[i][0] == op)
      return operation_priority[i][1];
  return 0;
  }

static Frac last_res;
static Frac variables[26];

typedef struct parcel {
  Frac frac;
  char operation;
  int parentesis_level;
  struct parcel *previous, *next;
  } Parcel;

/* In case there is an error and we have to quit, this will avoid
   the evil memory leaks. */
void force_cleanup(Parcel* first_parcel);

void algebric_calculator(char* str)
  {
  Parcel* first_parcel;  // keeps the position of the first parcel
  Parcel* parcel_i;      // used as index

  first_parcel = malloc(sizeof(Parcel));  // first malloc
  first_parcel->previous = (first_parcel->next = NULL);

  int max_parentesis_level = 0, parentesis_level = 0;

  remove_spaces(str);
  if(str[0] == '\0')
    return;  // nothing to do

  /* Parsing the input. */
  int i = 0;
  if(is_letter(str[0]) && str[1] == '=')  // there is a variable to equal
    i = 2;

  for(parcel_i = first_parcel;;
      parcel_i->next = malloc(sizeof(Parcel)),
      parcel_i->next->previous = parcel_i,
      parcel_i->next->next = NULL,
      parcel_i = parcel_i->next)
    {
    Frac* frac = &parcel_i->frac;
    ini_frac(frac);

    // getting numenator
    while(str[i] == '(')
      {
      parentesis_level++;
      if(parentesis_level > max_parentesis_level)
        max_parentesis_level = parentesis_level;
      i++;
      }
    parcel_i->parentesis_level = parentesis_level;

    if(str[i] == '-')
      {
      frac->signal = NEGATIVE;
      i++;
      }
    BOOL reading_variable = FALSE;
    if(!is_number(str[i]))
      {
      if(is_operation(str[i]) && i == 0)
        {
        if(parcel_i != first_parcel)
          SYNTAX_ERROR(str[i], "Two operations in a row.")
        if(last_res.error)
          INSTRUCTION_ERROR("No value was previously fed.", ' ')
        copy_frac(&last_res, frac);
        parcel_i->operation = str[i];
        i++;
        continue;
        }
      else if(is_letter(str[i]))
        {
        reading_variable = TRUE;
        SIGNAL signal = frac->signal;

        int index = convert_index(str[i]);
        if(variables[index].error)
          INSTRUCTION_ERROR("Variable not initializated yet or with an error value: ", str[i])
        copy_frac(&variables[index], frac);

        if(signal == NEGATIVE)
          frac->signal *= -1;
        i++;
        }
      else
        SYNTAX_ERROR(str[i], "Expecting a number.")
      }
    else
      for(;is_number(str[i]); i++)
        frac->num = (frac->num*10) + (str[i] - '0');

    // getting denominator
    if(!reading_variable && str[i] == '/' &&  // não usar fracções em expoentes
      (parcel_i->previous == NULL || parcel_i->previous->operation != '^')
      && is_number(str[i+1]))
      {  // introduced a fraccion
      for(i++; is_number(str[i]); i++)
        frac->den = (frac->den*10) + (str[i] - '0');
      }
    else if(str[i] == '.' || str[i] == ',')
      {  // introduced fraccionary part
      i++;
      if(!is_number(str[i]))
        SYNTAX_ERROR(str[i], "Fraccionary part expected.")
      frac->den = 1;
      for(;is_number(str[i]); i++)
        {
        frac->num = (frac->num*10) + (str[i] - '0');
        frac->den *= 10;
        }
      }
    else  // introduced an integer
      frac->den = 1;

    while(str[i] == ')')
      {
      parentesis_level--;
      i++;
      }
    if(parentesis_level < 0)
      SYNTAX_ERROR(str[i], "Too many closed parentesis!")

    if(frac->den == 0)
      INSTRUCTION_ERROR("Fraccion fed with denominator 0.", ' ')

    if(str[i] == '\0')
      {
      parcel_i->next = NULL;
      parcel_i->operation = ' ';
      break;
      }

    // getting the desired operation
    if(!is_operation(str[i]))
      {
      if(reading_variable || is_letter(str[i]) || str[i] == '(' || str[i] == ')')
        parcel_i->operation = '*';
      else
        SYNTAX_ERROR(str[i], "Expecting an operation symbol.");
      }
    else
      {
      parcel_i->operation = str[i];
      i++;
      }

    // following to the next parcel
    if(str[i] == '\0')
      SYNTAX_ERROR(str[i-1], "Nothing fed after the operator.")
    }

  /* Now let's process the information. */
  Parcel* res = first_parcel;
  int level, operational_priority;
  for(level = max_parentesis_level; level >= 0; level--)
    {
    for(operational_priority = MAX_PRIORITY;
        operational_priority >= 0; operational_priority--)
      {
      for(parcel_i = first_parcel->next;
          parcel_i != NULL;
          res = parcel_i, parcel_i = parcel_i->next)
        {
        if(parcel_i->previous->parentesis_level < level ||
           parcel_i->parentesis_level < level)
          continue;
        if(get_priority(parcel_i->previous->operation) != operational_priority)
          continue;

        Frac* frac_i   = &parcel_i->frac;
        Frac* frac_pre = &parcel_i->previous->frac;

        // efectuating the operation
        switch(parcel_i->previous->operation)
          {
          case '+':
            sum(frac_pre, frac_i, frac_i);
          break;
          case '-':
            subtract(frac_pre, frac_i, frac_i);
          break;
          case '*':
            multiply(frac_pre, frac_i, frac_i);
            break;
          case '/':
            divide(frac_pre, frac_i, frac_i);
            break;
          case '^':
            power(frac_pre, frac_i, frac_i);
            break;
          case ' ':
            // last parcel
            printf("Internal error: calculating last parcel as "
                   "if it was on of the middle.\n");
            force_cleanup(first_parcel);
            return;
          default:
            INSTRUCTION_ERROR("Unknown operation: ", parcel_i->previous->operation)
          }

        // taking care of freeing the parcel
        if(parcel_i->previous->previous)
          parcel_i->previous->previous->next = parcel_i;
        else  // first parcel
          first_parcel = parcel_i;
        free(parcel_i->previous);
        parcel_i->previous = parcel_i->previous->previous;
        }
      }
    }

  if(first_parcel == res)
    simplify(&first_parcel->frac);  // since there was nothing to process, at least
                                    // let's burn some cpu simplifying the only fraccion fed
  print_frac("", &res->frac);

  if(!res->frac.error)
    copy_frac(&res->frac, &last_res);
  if(is_letter(str[0]) && str[1] == '=')  // equals to variable
    copy_frac(&res->frac, &variables[convert_index(str[0])]);

  free(res);
  }

BOOL strcomp(const char s1[], const char s2[]);

#ifdef READLINE_ENABLED
  #include <readline/readline.h>
  #include <readline/history.h>
#else
  #define STR_SIZE 2048
#endif

int main()
  {
    {
    // not exactly the best place to initializate stuff, but...
    last_res.error = TRUE;
    int i;
    for(i = 0; i < 26; i++)
      variables[i].error = TRUE;
    }

  printf("-> SuperFrac, your neat yet powerfull algebric expressions calculator <-\n");
  while(TRUE)
    {
    #ifdef READLINE_ENABLED
      char* str = readline("> ");
      if(str == NULL)
        {
        putchar('\n');
        break;
        }
      if(str[0] == '\0')
        continue;
      add_history(str);
    #else
      char str[STR_SIZE];
      printf("> ");
      {  // get input
      int i;
      for(i = 0; i < STR_SIZE; i++)
        {
        str[i] = getchar();
        if(str[i] == '\n' || str[i] == '\0' || str[i] == EOF)
          break;
        }
      if(str[i] == EOF)
        {
        putchar('\n');
        break;
        }
      if(i == STR_SIZE)
        {
        printf("Error: maximum string size reached.\n");
        continue;
        }
      str[i] = '\0';
      }
    #endif

    if(strcomp(str, "help") || strcomp(str, "h")  || strcomp(str, "?"))
      printf("\t-= Help =-\nInsert an algebric expression.\n\n"
             "Parentesis and the operations +,-,/,* and ^ are supported.\n"
             "Exemple: 2/5*((3/10+5)^4)\n\n"
             "Variables (from 'a' to 'z') are supported as well.\n"
             "Exemple: a=10*5\n"
             "         5/2*a\n\n"
             "Type \"quit\" to exit.\n\n");
    else if(strcomp(str, "quit") || strcomp(str, "exit")  || strcomp(str, "q"))
      break;
    else
      algebric_calculator(str);

    #ifdef READLINE_ENABLED
      free(str);
    #endif
    }
  return 0;
  }

void force_cleanup(Parcel* p)
  {
  Parcel* i;
  while(p != NULL)
    {
    i = p;
    p = p->next;
    free(i);
    }
  }

void remove_spaces(char str[])
  {
  int i, j;
  i = 0; j = 0;
  for(i = (j = 0); str[i] != '\0'; i++)
    if(str[i] != ' ')
      str[j++] = str[i];
  str[j] = '\0';
  }

BOOL strcomp(const char s1[], const char s2[])
  {
  int i;
  for(i = 0; s1[i] != '\0' && s2[i] != '\0'; i++)
    if(s1[i] != s2[i])
      return FALSE;
  return s1[i] == s2[i];
  }
