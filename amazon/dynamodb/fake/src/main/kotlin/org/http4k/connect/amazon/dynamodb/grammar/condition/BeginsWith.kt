package org.http4k.connect.amazon.dynamodb.grammar.condition

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import parser4k.Parser
import parser4k.commonparsers.token
import parser4k.inOrder
import parser4k.map
import parser4k.ref

object BeginsWith : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        inOrder(token("begins_with"), token("("), ref(parser), token(","), ref(parser), token(")"))
            .map { (_, _, attr, _, value) ->
                Expr { item: ItemWithSubstitutions ->
                    (attr.eval(item).asString().toString()).startsWith(
                        value.eval(item).asString().toString()
                    )
                }
            }
}

