package org.http4k.connect.amazon.dynamodb.grammar.projection

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.grammar.binaryExpr
import org.http4k.connect.amazon.dynamodb.grammar.condition.AttributeNameValue
import parser4k.Parser
import parser4k.ref


object ProjectionList : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        binaryExpr(ref(parser), ",") { left, right ->
            Expr {
                (left.eval(it) as List<AttributeNameValue>) + (right.eval(it) as List<AttributeNameValue>)
            }
        }
}
