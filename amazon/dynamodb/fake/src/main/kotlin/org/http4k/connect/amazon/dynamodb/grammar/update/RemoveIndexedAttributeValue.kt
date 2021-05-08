package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.model.AttributeValue
import parser4k.Parser
import parser4k.commonparsers.Tokens.number
import parser4k.commonparsers.token
import parser4k.inOrder
import parser4k.mapLeftAssoc
import parser4k.ref

object RemoveIndexedAttributeValue : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        inOrder(ref(parser), token("["), number, token("]"))
            .mapLeftAssoc { (expr, _, index) ->
                Expr { item ->
                    val name = expr.eval(item)
                    println("expr" + expr)
                    println("ater" + expr.eval(item))
                    println("name" + name)
                    println(item)
                    val existingList = item.item[name]!!.L!!
                    when {
                        existingList.size > index.toInt() + 1 -> item.item
                        else -> item.item +
                            (name to AttributeValue.List(
                                existingList
                                    .filterIndexed { i, _ -> i != index.toInt() }
                            )
                                )
                    }
                }
            }
}
