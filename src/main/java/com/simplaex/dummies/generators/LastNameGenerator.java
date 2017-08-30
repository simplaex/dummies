package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LastNameGenerator implements Generator<String> {

  private final Dummies dummies;

  private final static String[] lastNames;

  static {
    val reader = new BufferedReader(new InputStreamReader(LastNameGenerator.class.getResourceAsStream("last-names.txt")));
    val list = reader.lines().collect(Collectors.toList());
    lastNames = list.toArray(new String[list.size()]);
  }

  @Override
  public String get() {
    return lastNames[dummies.getRandom().nextInt(lastNames.length)];
  }
}
