package shitcompiler.parser

import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 26/01/17.
 */

val EXPRESSION_SYMBOLS = setOf(PLUS, MINUS, AND, OR)
val TERM_SYMBOLS = setOf(ASTERISK, DIV, MOD, GREATER, NOT_GREATER, LESSER, NOT_LESSER, EQUAL, NOT_EQUAL)
val UNARY_SYMBOLS = setOf(PLUS, MINUS, NOT)

val ASSIGN_INT_SYMBOLS = setOf(BECOMES_PLUS, BECOMES_MINUS, BECOMES_TIMES, BECOMES_DIV, BECOMES_MOD)
val ASSIGN_OP_SYMBOLS = ASSIGN_INT_SYMBOLS + setOf(BECOMES)

val DECLARATION_SYMBOLS = setOf(INT, BOOL, CHAR, STRUCT, VOID, SEMICOLON)
val STATEMENT_SYMBOLS = DECLARATION_SYMBOLS + setOf(ID, ASTERISK)

val TYPE_QUALIFIER_SYMBOLS = setOf(LEFT_BRACKET, ASTERISK)
val SELECTOR_SYMBOLS = setOf(LEFT_BRACKET, PERIOD, RIGHT_ARROW)