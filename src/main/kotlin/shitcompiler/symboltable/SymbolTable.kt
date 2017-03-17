package shitcompiler.symboltable

import shitcompiler.NO_NAME
import shitcompiler.UNNAMED
import shitcompiler.ast.type.ArrayTypeReference
import shitcompiler.ast.type.TypeReference
import shitcompiler.symboltable.classes.ArrayType
import shitcompiler.symboltable.classes.Constant
import shitcompiler.symboltable.classes.Nothing
import java.io.PrintWriter

/**
 * Created by NuclearCoder on 06/03/17.
 */

class SymbolTable(private val errors: PrintWriter) {

    val typeUniversal: ObjectRecord
    val typeInt: ObjectRecord
    val typeChar: ObjectRecord
    val typeBool: ObjectRecord
    val stdTrue: ObjectRecord
    val stdFalse: ObjectRecord

    private val blocks = arrayListOf(BlockRecord(0))

    // the 0-th level is the standard level
    private var currentLevel: Int = 0

    init {
        var i = 0

        typeUniversal = define(i++, Kind.STANDARD_TYPE)
        typeInt = define(i++, Kind.STANDARD_TYPE)
        typeChar = define(i++, Kind.STANDARD_TYPE)
        typeBool = define(i++, Kind.STANDARD_TYPE)

        stdTrue = define(i++, Kind.CONSTANT, Constant(1, typeBool))
        stdFalse = define(i, Kind.CONSTANT, Constant(0, typeBool))
    }

    fun findOrDefineType(type: TypeReference): ObjectRecord {
        val elementType = if (type is ArrayTypeReference) {
            findOrDefineType(type.elementType)
        } else {
            find(type.name)
        }

        if (elementType.kind != Kind.STANDARD_TYPE
                && elementType.kind != Kind.ARRAY_TYPE
                && elementType.kind != Kind.STRUCT_TYPE) {
            errors.println("Type reference was ${elementType.kind}, expected ${Kind.STANDARD_TYPE}, ${Kind.ARRAY_TYPE} or ${Kind.STRUCT_TYPE}")
            return typeUniversal
        }

        if (type is ArrayTypeReference) {
            val elementTypeBlock = blocks[elementType.blockLevel]
            val length = type.length

            // array types are defined in the same level as the element type
            val obj = elementTypeBlock.firstOrNull {
                it.name == UNNAMED && it.kind == Kind.ARRAY_TYPE
                        && it.asArrayType().elementType == elementType
                        && it.asArrayType().length == length
            }

            if (obj != null) return obj
            return elementTypeBlock.define(UNNAMED, Kind.ARRAY_TYPE, ArrayType(elementType, length))
        } else {
            // if standard type, just return
            return elementType
        }
    }

    fun find(name: Int): ObjectRecord {
        for (level in currentLevel downTo 0) {
            val obj = blocks[level].find(name)
            if (obj != null) {
                return obj
            }
        }
        errors.println("Unknown reference $name")
        return define(name, Kind.UNDEFINED)
    }

    fun define(name: Int, kind: Kind, data: ObjectClass = Nothing): ObjectRecord {
        if (name != NO_NAME && name != UNNAMED && blocks[currentLevel].find(name) != null) {
            errors.println("Defined $name more than once")
        }
        return blocks[currentLevel].define(name, kind, data)
    }

    fun beginBlock() {
        currentLevel++
        if (currentLevel == blocks.size) {
            blocks.add(BlockRecord(currentLevel))
        }
    }

    fun endBlock() {
        val block = blocks[currentLevel]
        block.clear()
        currentLevel--
    }

}