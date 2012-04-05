package org.lodge.street

class TreeTagLib {
  static namespace = "tree"

  def tree = { attrs, body ->
    out << "<h3>${attrs.title}</h3>"
    out << ol(attrs.data, attrs.node ?: { it.toString() }, attrs.leaf ?: { it.toString() })
  }

  def ol(item, node = { it.toString() }, leaf = { it.toString() }) {
    ifLeaf(item) ? "${leaf(item)}" : item.inject("<ul>", { ctx, curr ->
      "${ctx}<li>${isEntry(curr) ? node(curr.key) + ol(curr.value, node, leaf) : ol(curr, node, leaf)}</li>" 
    }) + "</ul>"
  }

  def isEntry(item) {
    Map.Entry.isAssignableFrom(item.getClass())
  }

  def ifLeaf(item) {
    (Map.isAssignableFrom(item.getClass()) && item.leaf) || !(List.isAssignableFrom(item.getClass()) || Map.isAssignableFrom(item.getClass()))
  }

}
