package org.http4k.connect.amazon.dynamodb.grammar.condition

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.grammar.binaryExpr
import parser4k.Parser
import parser4k.ref

object And : (() -> Parser<Expr>) -> Parser<Expr> {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        binaryExpr(ref(parser), "AND", ::And)
}

fun And(left: Expr, right: Expr) = Expr { item: ItemWithSubstitutions ->
    (left.eval(item) as Boolean) && (right.eval(item) as Boolean)
}

