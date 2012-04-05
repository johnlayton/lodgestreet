package org.lodge.street

class TreeBootStrap {

  def init = { servletContext ->
    List.metaClass.toTree = { config = { prop([]) } ->
      new Tree(config).evaluate(delegate)
    }
  }

  def destroy = {
  }
}

