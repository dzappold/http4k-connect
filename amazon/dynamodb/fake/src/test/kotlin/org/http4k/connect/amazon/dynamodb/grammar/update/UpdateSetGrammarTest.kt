package org.http4k.connect.amazon.dynamodb.grammar.update

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.connect.amazon.dynamodb.model.Item
import org.junit.jupiter.api.Test

class UpdateSetGrammarTest {

    private val attrNum = Attribute.int().required("attrNum")
    private val attr1 = Attribute.string().required("attr1")
    private val attrList = Attribute.list().required("attrList")
    private val attrMap = Attribute.map().required("attrMap")

    @Test
    fun `set value`() {
        assertThat(
            UpdateSetGrammar.parse("#attr = :value").eval(
                ItemWithSubstitutions(
                    Item(attr1 of "222", attrNum of 456),
                    mapOf("#attr" to attr1.name),
                    mapOf(":value" to attr1.asValue("333"))
                )
            ),
            equalTo(Item(attr1 of "333", attrNum of 456))
        )
    }
}
