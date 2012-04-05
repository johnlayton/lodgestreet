package org.lodge.street

class Tree {

  def clos = [{ it.toString() }]
  def node = { it }
  def leaf = { it }

  def makeNode
  def makeLeaf

  Tree(config) {
    config.resolveStrategy = Closure.DELEGATE_FIRST
    config.delegate = this
    config.call()
  }

  def prop(String props) {
    prop(props.split("\\,"))
  }

  def prop(String[] props) {
    prop(props as List)
  }

  def prop(List props) {
    clos(props.collect({ name ->
        { bean -> name.split("\\.").inject(bean, { ibean, iname -> ibean?."${iname}"  })  }
      })
    )
  }

  def clos(Closure clos) {
    this.clos = [clos]
  }

  def clos(Closure[] closures) {
    this.clos = closures
  }

  def clos(List<Closure> closures) {
    this.clos = closures
  }

  def node(Closure node) {
    this.node = node
    this.makeNode = true
  }

  def leaf(String props) {
    leaf(props.split(','))
  }

  def leaf(String[] props) {
    leaf(props as List)
  }

  def leaf(List props) {
    leaf({ item ->
      props.inject([leaf: true], { map, name ->
        map << [ (name): name.split("\\.").inject(item, { bean, field ->
          List.isAssignableFrom(item.getClass()) ? bean*."${field}" : bean?."${field}" })]
      })
    })
  }

  def leaf(Closure leaf) {
    this.leaf = leaf
  }

  def evaluate(list) {
    toTree(list, clos, node, leaf)
  }

  def toTree(List list, List closures = clos, Closure node = { it }, Closure leaf = { it }) {
    if(closures && !closures.empty) {
      def result = list.groupBy(closures.head()).each({ entry ->
        entry.value = closures.tail().empty ? entry.value.collect(leaf) : toTree(entry.value, closures.tail(), node, leaf)
      }).sort({ a,b -> a.key <=> b.key })
      if (makeNode)
        result.collect(node)
      else
        result
    }
    else
      list.collect(leaf)
  }
}

