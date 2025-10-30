package aud.aud3.kolokviumska;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExecuteAndSort <T>{

    public static <T extends Comparable<T>> List<T> execute(List<T> list, Function<T, T> function) {
        return list.stream().map(function).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

}

