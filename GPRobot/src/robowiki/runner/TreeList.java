package robowiki.runner;

import ga.GATree;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class TreeList {
  private List<GATree> _trees;

  public TreeList(GATree tree) {
    _trees = Lists.newArrayList(tree);
  }

  public TreeList(List<GATree> trees) {
    _trees = Lists.newArrayList(trees);
  }

  public List<GATree> getTrees() {
    return ImmutableList.copyOf(_trees);
  }
}
