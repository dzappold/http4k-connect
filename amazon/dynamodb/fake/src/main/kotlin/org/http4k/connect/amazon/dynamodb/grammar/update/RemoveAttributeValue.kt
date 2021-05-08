package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import parser4k.Parser
import parser4k.commonparsers.token
import parser4k.inOrder
import parser4k.map
import parser4k.ref
import parser4k.skipFirst

object RemoveAttributeValue : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        inOrder(token("REMOVE"), ref(parser)).skipFirst().map {
            Expr { item ->
                item.item - it.eval(item)
            }
        }
}
