package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.grammar.binaryExpr
import org.http4k.connect.amazon.dynamodb.model.AttributeName
import parser4k.Parser
import parser4k.ref

object SetValue : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        binaryExpr(ref(parser), "=") { left, right ->
            Expr {
                it.item + (left.eval(it) as AttributeName to right.eval(it))
            }
        }
}

