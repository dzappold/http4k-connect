package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.grammar.binaryExpr
import parser4k.Parser
import parser4k.ref

object UpdateList : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        binaryExpr(ref(parser), ",") { left, right ->
            Expr {
                (right.eval((left.eval(it) as ItemWithSubstitutions)) as ItemWithSubstitutions)
            }
        }
}
