package org.http4k.connect.amazon.dynamodb.grammar

import parser4k.Parser
import parser4k.asBinary
import parser4k.commonparsers.token
import parser4k.inOrder
import parser4k.map
import parser4k.mapLeftAssoc

fun interface Expr {
    fun eval(item: ItemWithSubstitutions): Any
}

typealias ExprFactory = (() -> Parser<Expr>) -> Parser<Expr>

fun binaryExpr(parser: Parser<Expr>, tokenString: String, f: (Expr, Expr) -> Expr) =
    inOrder(parser, token(tokenString), parser).mapLeftAssoc(f.asBinary())

fun unaryExpr(parser: Parser<Expr>, tokenString: String, f: (Expr) -> Expr) =
    inOrder(token(tokenString), parser).map { (_, it) -> f(it) }

