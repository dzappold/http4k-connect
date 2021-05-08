package org.http4k.connect.amazon.dynamodb.grammar.condition

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.grammar.binaryExpr
import parser4k.Parser
import parser4k.ref

object LessThanOrEqual : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        binaryExpr(ref(parser), "<=", ::LessThanOrEqual)
}

fun LessThanOrEqual(attr1: Expr, attr2: Expr) = Expr { item ->
    item.comparable(attr1) <= item.comparable(attr2)
}
