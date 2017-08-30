package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FirstNameGenerator implements Generator<String> {

  private final Dummies dummies;

  private final static String[] firstNames;

  static {
    val reader = new BufferedReader(new InputStreamReader(FirstNameGenerator.class.getResourceAsStream("first-names.txt")));
    val list = reader.lines().collect(Collectors.toList());
    firstNames = list.toArray(new String[list.size()]);
  }

  @Override
  public String get() {
    return firstNames[dummies.getRandom().nextInt(firstNames.length)];
  }
}
