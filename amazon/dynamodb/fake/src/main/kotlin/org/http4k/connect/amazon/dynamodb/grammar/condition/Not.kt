package org.http4k.connect.amazon.dynamodb.grammar.condition

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.grammar.unaryExpr
import parser4k.Parser
import parser4k.ref

object Not : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>) =
        unaryExpr(ref(parser), "NOT") { expr ->
            Expr { !(expr.eval(it) as Boolean) }
        }
}
