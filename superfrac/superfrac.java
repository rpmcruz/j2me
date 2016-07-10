/***************************************************************************
                        superfrac.java  -  parser and processor
                             -------------------
    begin                : Tue Nov 15 2005
    copyright            : (C) 2005 by Ricardo Cruz
    email                : rpmcruz@clix.pt
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

import java.util.*;

/* Reads the input. */
class SuperFrac
  {
  private static Calculator calculator;
  private static final String help =
    "\t-= Help =-\nInsert an algebric expression.\n\n" +
    "Parentesis and the operations +,-,/,* and ^ are supported.\n" +
    "Exemple: 2/5*((3/10+5)^4)\n\n" +
    "Variables (from 'a' to 'z') are supported as well.\n" +
    "Exemple: a=10*5\n" +
    "         5/2*a\n\n" +
    "Type \"quit\" to exit.\n\n";

  public static void main(String argv[])
    {
    calculator = new Calculator();

    System.out.println("-> SuperFrac, your neat yet powerfull algebric " +
                       "expressions calculator <-");

    Scanner stdin = new Scanner(System.in);
    while(stdin.hasNext())
      {
//      System.out.print("> ");  // damn Scanner that eats this
      String input = stdin.nextLine();

      if(input.compareTo("quit") == 0 || input.compareTo("exit") == 0)
        break;
      if(input.compareTo("help") == 0)
        System.out.print(help);

      calculator.calculate(input);
      }
//    System.out.println();
    }
  }

/* Parses and computes the input in an arithmetic manner. */
class Calculator
  {
  private static final boolean NEGATIVE = false, POSITIVE = true;

  Calculator()
    {
    last_result = new Fraction();
    last_result.error = true;
    variables = new Fraction [26];
    for(int i = 0; i < 26; i++)
      {
      variables[i] = new Fraction();
      variables[i].error =  true;
      }
    }

  boolean is_number(char c)
    { return c >= '0' && c <= '9'; }
  boolean is_letter(char c)
    { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'); }
  int convert_index(char c)
    { if(c >= 'A' && c <= 'Z') return c - 'A'; return c - 'a'; }
  boolean is_operation(char c)
    { return c == '^' || c == '*' || c == '/' || c == '+' || c == '-'; }

  void SYNTAX_ERROR(char c, String s)
    { System.out.printf("Syntax error: %s\nCharacter not expected: '%c'.\n", s,c); }
  void CALCULUS_ERROR(char c, String s)
    { System.out.printf("Calculus error on %c.\nProbable cause: %s\n", c,s); }
  void INSTRUCTION_ERROR(String s, char c)
    { System.out.printf("Instruction error: %s%c.\n", s, c); }

  final static int MAX_PRIORITY = 2;
  int get_priority(char op)
    {
    switch(op)
      {
      case '^':
        return 2;
      case '*':
        return 1;
      case '/':
        return 1;
      case '+':
        return 0;
      case '-':
        return 0;
      }
    return 0;
    }

  /* Converts a String to an array of chars and also eliminates white
     spaces. */
  char[] convert_to_array(String str)
    {
    char [] array = new char [str.length()+1];
    int str_i, array_i;
    for(str_i = 0, array_i = 0; str_i < str.length(); str_i++)
      if(str.charAt(str_i) != ' ' && str.charAt(str_i) != '\n')
        array[array_i++] = str.charAt(str_i);
    array[array_i] = '\0';
    return array;
    }

  void calculate(String string)
    {
    /** Converting the string to a char array -- on the mobile phone,
        this won't be necessary as we will make our own input method. */
    char [] str = convert_to_array(string);

    /** Translating to internal representation. */
    Parcel first_parcel;  // keeps the position of the first parcel
    Parcel parcel_i;      // used as index

    first_parcel = new Parcel();  // first malloc
    first_parcel.previous = (first_parcel.next = null);

    int max_parentesis_level = 0, parentesis_level = 0;

    // remove_spaces(str);  // spaces are already removed so this is uneeded
    if(str[0] == '\0')
      return;  // nothing to do

    /* Parsing the input. */
    int i = 0;
    if(is_letter(str[0]) && str[1] == '=')  // there is a variable to equal
      i = 2;

    for(parcel_i = first_parcel; ;
        parcel_i.next = new Parcel(),
        parcel_i.next.previous = parcel_i,
        parcel_i.next.next = null,
        parcel_i = parcel_i.next)
      {
      Fraction frac = parcel_i.fraction;

      // getting numenator
      while(str[i] == '(')
        {
        parentesis_level++;
        if(parentesis_level > max_parentesis_level)
          max_parentesis_level = parentesis_level;
        i++;
        }
      parcel_i.parentesis_level = parentesis_level;

      if(str[i] == '-')
        {
        frac.signal = NEGATIVE;
        i++;
        }

      boolean reading_variable = false;
      if(!is_number(str[i]))
        {
        if(is_operation(str[i]) && i == 0)
          {
          if(parcel_i != first_parcel)
            {
            SYNTAX_ERROR(str[i], "Two operations in a row.");
            return;
            }
          if(last_result.error)
            {
            INSTRUCTION_ERROR("No value was previously fed.", ' ');
            return;
            }
          frac.copy(last_result);
          parcel_i.operation = str[i];
          i++;
          continue;
          }
        else if(is_letter(str[i]))
          {
          reading_variable = true;
          boolean signal = frac.signal;

          int index = convert_index(str[i]);
          if(variables[index].error)
            {
            INSTRUCTION_ERROR("Variable not initializated yet or with an error value: ",
                              str[i]);
            return;
            }
          frac.copy(variables[index]);
          if(signal == NEGATIVE)
            frac.signal = POSITIVE;
          i++;
          }
        else
          {
          SYNTAX_ERROR(str[i], "Expecting a number.");
          return;
          }
        }
      else
        {
        for(; is_number(str[i]); i++)
          frac.numerator = (frac.numerator*10) + (str[i] - '0');
        }

      // getting denominator
      if(!reading_variable && str[i] == '/' &&  // não usar fracções em expoentes
        (parcel_i.previous == null || parcel_i.previous.operation != '^')
        && is_number(str[i+1]))
        {  // introduced a fraccion
        for(i++; is_number(str[i]); i++)
          frac.denominator = (frac.denominator*10) + (str[i] - '0');
        }
      else if(str[i] == '.' || str[i] == ',')
        {  // introduced fraccionary part
        i++;
        if(!is_number(str[i]))
          {
          SYNTAX_ERROR(str[i], "Fraccionary part expected.");
          return;
          }
        frac.denominator = 1;
        for(; is_number(str[i]); i++)
          {
          frac.numerator = (frac.numerator*10) + (str[i] - '0');
          frac.denominator *= 10;
          }
        }
      else  // introduced an integer
        frac.denominator = 1;

      while(str[i] == ')')
        {
        parentesis_level--;
        i++;
        }
      if(parentesis_level < 0)
        {
        SYNTAX_ERROR(str[i], "Too many closed parentesis!");
        return;
        }

      if(frac.denominator == 0)
        {
        INSTRUCTION_ERROR("Fraccion fed with denominator 0.", ' ');
        return;
        }

      if(str[i] == '\0')
        {
        parcel_i.next = null;
        parcel_i.operation = ' ';
        break;
        }

      // getting the desired operation
      if(!is_operation(str[i]))
        {
        if(reading_variable || is_letter(str[i]) || str[i] == '(' || str[i] == ')')
          parcel_i.operation = '*';
        else
          {
          SYNTAX_ERROR(str[i], "Expecting an operation symbol.");
          return;
          }
        }
      else
        {
        parcel_i.operation = str[i];
        i++;
        }

      // following to the next parcel
      if(str[i] == '\0')
        {
        SYNTAX_ERROR(str[i-1], "Nothing fed after the operator.");
        return;
        }
      }

    /** Now processing the calculation. */
    Parcel res = first_parcel;
    int level, operational_priority;
    for(level = max_parentesis_level; level >= 0; level--)
      {
      for(operational_priority = MAX_PRIORITY;
          operational_priority >= 0; operational_priority--)
        {
        for(parcel_i = first_parcel.next;
            parcel_i != null;
            res = parcel_i, parcel_i = parcel_i.next)
          {
          if(parcel_i.previous.parentesis_level < level ||
            parcel_i.parentesis_level < level)
            continue;
          if(get_priority(parcel_i.previous.operation) != operational_priority)
            continue;

          Fraction frac_i   = parcel_i.fraction;
          Fraction frac_pre = parcel_i.previous.fraction;

          // efectuating the operation
          switch(parcel_i.previous.operation)
            {
            case '+':
              Operations.sum(frac_pre, frac_i, frac_i);
              break;
            case '-':
              Operations.subtract(frac_pre, frac_i, frac_i);
              break;
            case '*':
              Operations.multiply(frac_pre, frac_i, frac_i);
              break;
            case '/':
              Operations.divide(frac_pre, frac_i, frac_i);
              break;
            case '^':
              Operations.power(frac_pre, frac_i, frac_i);
              break;
            case ' ':
              // last parcel
              System.out.println("Internal error: calculating last parcel as " +
                                 "if it was on of the middle.\n");
              return;
            default:
              {
              INSTRUCTION_ERROR("Unknown operation: ", parcel_i.previous.operation);
              return;
              }
            }

          // taking care of freeing the parcel
          if(parcel_i.previous.previous != null)
            parcel_i.previous.previous.next = parcel_i;
          else  // first parcel
            first_parcel = parcel_i;
//          free(parcel_i.previous);
          parcel_i.previous = parcel_i.previous.previous;
          }
        }
      }

    /* Printing and last touches. */
    if(first_parcel == res)
      // since there was nothing to process, at least
      // let's burn some cpu simplifying the only fraccion fed
      Operations.simplify(first_parcel.fraction);

    res.fraction.print("");  // print result

    if(!res.fraction.error)
      last_result.copy(res.fraction);
    if(is_letter(str[0]) && str[1] == '=')  // equals to variable
      variables[convert_index(str[0])].copy(res.fraction);
    }

  private Fraction last_result;
  private Fraction [] variables;

  class Parcel
    {
    Parcel()
      { fraction = new Fraction();
        operation = ' '; parentesis_level = 0;
        previous = next = null; }
    Parcel(Fraction f, char o, int pl)
      {
      fraction = f; operation = o; parentesis_level = pl;
      previous = next = null;
      }

    Fraction fraction;
    char operation;
    int parentesis_level;
    Parcel previous, next;
    }
  }

class Fraction
  {
  private static final boolean NEGATIVE = false, POSITIVE = true;

  Fraction()
    { signal = POSITIVE; numerator = denominator = 0; error = false; }
  Fraction(boolean s, int n, int d)
    { signal = s; numerator = n; denominator = d; error = false; }

  void copy(Fraction f)
    { signal = f.signal; numerator = f.numerator; denominator = f.denominator;
      error = f.error; }

  void print(String label)
    {
    if(denominator == 1)  // print integer
      System.out.printf("%s= %c%d\n", label, (signal == NEGATIVE) ? '-' : ' ', numerator);

    else if(Operations.is_10X(denominator))
      {  // fraccaionary of base 10, print as racional
      System.out.printf("%s= %c", label, (signal == NEGATIVE) ? '-' : ' ');

      int num = numerator, den = denominator;
      if(num*10 > den)
        System.out.printf("%d", num/den);
      else
        System.out.print('0');
      System.out.print('.');

      for(den /= 10; den > 0; den /= 10)
        System.out.print((num/den) - (num/(den*10)*10) + '0');
      System.out.print('\n');
      }

    else  // fraccionary
      System.out.printf("%s= %c%d/%d\n", label, (signal == NEGATIVE) ? '-' : ' ',
                        numerator, denominator);
    }

  boolean signal;
  /*unsigned*/ int numerator, denominator;  // should we change to long?
  boolean error;
  }

class Operations
  {
  private static final boolean NEGATIVE = false, POSITIVE = true;
  static int signal_value(boolean sign)
    { if(sign) return 1; return -1; }

  /** Operations on fractions. */
  static void simplify(Fraction f)
    {
    int mdc = gcd(f.numerator, f.denominator);
    f.numerator /= mdc;
    f.denominator /= mdc;
    }
  static void sum(final Fraction f1, final Fraction f2, Fraction res)
    {
    int num = (f1.numerator * f2.denominator * signal_value(f1.signal)) +
              (f2.numerator * f1.denominator * signal_value(f2.signal));
    if(num < 0)
      {
      res.signal = NEGATIVE;
      res.numerator = -num;
      }
    else
      {
      res.signal = POSITIVE;
      res.numerator = num;
      }

    res.denominator = f1.denominator * f2.denominator;
    simplify(res);
    res.error = false;
    }

  static void subtract(final Fraction f1, final Fraction f2, Fraction res)
    {
    int num = (f1.numerator * f2.denominator * signal_value(f1.signal)) +
              (f2.numerator * f1.denominator * signal_value(!f2.signal));
    if(num < 0)
      {
      res.signal = NEGATIVE;
      res.numerator    = -num;
      }
    else
      {
      res.signal = POSITIVE;
      res.numerator    = num;
      }
    res.denominator = f1.denominator * f2.denominator;
    simplify(res);
    res.error = false;
    }

  static void multiply(final Fraction f1, final Fraction f2, Fraction res)
    {
    res.numerator = f1.numerator * f2.numerator;
    res.denominator = f1.denominator * f2.denominator;
    simplify(res);
    res.signal = (f1.signal == f2.signal) ? POSITIVE : NEGATIVE;
    res.error = false;
    }

  static void divide(final Fraction f1, final Fraction f2, Fraction res)
    {
    int num = f1.numerator * f2.denominator;
    int den = f1.denominator * f2.numerator;

    res.numerator = num;
    res.denominator = den;
    simplify(res);

    res.signal = (f1.signal == f2.signal) ? POSITIVE : NEGATIVE;
    if(res.denominator == 0)
      res.error = true;
    else
      res.error = false;
    }

  static void power(final Fraction f1, final Fraction f2, Fraction res)
    {
    int num = f1.numerator;
    int den = f1.denominator;
    if(f2.signal == NEGATIVE)
      {
//      swap(&num, &den);
      int t = num;
      num = den;
      den = t;
      }
    num = power(num, f2.numerator);
    den = power(den, f2.numerator);
    res.numerator = num;
    res.denominator = den;
    if(f1.signal == NEGATIVE && f2.numerator % 2 != 0)
      res.signal = NEGATIVE;
    if(f2.denominator != 1)
      res.error = true;
    else
      res.error = false;
    }

  /** Operations on numbers. */
  /* Calculates the Maximum Common Divider by the Euclides algorithm. */
  static int gcd(int a, int b)
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
  /* expoent must be positive! */
  static int power(int base, int exp)
    {
    if(exp < 0)
      {
      System.out.println("Internal error: expoent must be positive.\n");
      return 0;
      }
    if(exp == 0)
      return 1;
    int b = base;
    for(; exp > 1; exp--)
      base *= b;
    return base;
    }
  /* Check if the number is 10, 100, 1000 or anything that has a 1 and
     zeros. */
  static boolean is_10X(int n)
    {
    for(; n > 1; n /= 10)
      if(n % 10 != 0)
        return false;
    return true;
    }
  }
