package org.http4k.connect.amazon.dynamodb.grammar.update

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.connect.amazon.dynamodb.model.Item
import org.junit.jupiter.api.Test

class UpdateRemoveGrammarTest {

    private val attrNum = Attribute.int().required("attrNum")
    private val attr1 = Attribute.string().required("attr1")
    private val attrList = Attribute.list().required("attrList")
    private val attrMap = Attribute.map().required("attrMap")

    @Test
    fun `remove value`() {
        assertThat(
            UpdateRemoveGrammar.parse("REMOVE #attr").eval(
                ItemWithSubstitutions(
                    Item(
                        attr1 of "222",
                        attrNum of 456
                    ),
                    mapOf("#attr" to attr1.name)
                )
            ),
            equalTo(
                Item(
                    attrNum of 456
                )
            )
        )
    }

    @Test
    fun `remove indexed value`() {
        assertThat(
            UpdateRemoveGrammar.parse("REMOVE #attr[1]").eval(
                ItemWithSubstitutions(
                    Item(
                        attr1 of "222",
                        attrList of listOf(attr1.asValue("333"), attr1.asValue("444"))
                    ),
                    mapOf("#attr" to attrList.name)
                )
            ),
            equalTo(
                Item(
                    attr1 of "222",
                    attrList of listOf(attr1.asValue("444"))
                )
            )
        )
    }
}
