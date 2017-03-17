package shitcompiler.ast.type

import shitcompiler.ast.AST

/**
 * Created by NuclearCoder on 16/03/17.
 */

// if length is negative, the type is not considered an array
open class TypeReference(val name: Int) : AST {

    override fun toString() = "type($name)"

}