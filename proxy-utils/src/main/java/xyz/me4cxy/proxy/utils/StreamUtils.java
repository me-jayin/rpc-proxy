package xyz.me4cxy.proxy.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 流式编程工具类
 *
 * @author jayin
 * @since 2024/01/07
 */
public class StreamUtils {
    private StreamUtils() { }

    /**
     * 过滤元素
     * @param lists
     * @param predicate
     * @return
     * @param <T>
     */
    public static <T> List<T> filter(Collection<T> lists, Predicate<T> predicate) {
        return lists.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 过滤指定元素进行遍历操作
     * @param lists
     * @param predicate
     * @return
     * @param <T>
     */
    public static <T> List<T> filter(List<T> lists, Predicate<T> predicate) {
        return lists.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 过滤指定元素进行遍历操作
     * @param lists
     * @param predicate
     * @return
     * @param <T>
     */
    public static <T> Set<T> filter(Set<T> lists, Predicate<T> predicate) {
        return lists.stream().filter(predicate).collect(Collectors.toSet());
    }

    /**
     * 遍历list执行操作
     * @param lists
     * @param action
     * @return
     * @param <T>
     */
    public static <T> List<T> peek(List<T> lists, Consumer<T> action) {
        return lists.stream().peek(action).collect(Collectors.toList());
    }

    /**
     * 遍历set执行操作
     * @param lists
     * @param action
     * @return
     * @param <T>
     */
    public static <T> Set<T> peek(Set<T> lists, Consumer<T> action) {
        return lists.stream().peek(action).collect(Collectors.toSet());
    }

    /**
     * 对list进行映射
     * @param lists
     * @param mapping
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T, R> List<R> mapping(List<T> lists, Function<T, R> mapping) {
        return lists.stream().map(mapping).collect(Collectors.toList());
    }

    /**
     * 对set进行映射
     * @param lists
     * @param mapping
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T, R> Set<R> mapping(Set<T> lists, Function<T, R> mapping) {
        return lists.stream().map(mapping).collect(Collectors.toSet());
    }

    /**
     * 将list进行展开
     * @param lists
     * @param mapper
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T, R> List<R> flatMap(List<T> lists, Function<T, Collection<R>> mapper) {
        return lists.stream().map(mapper).filter(CollectionUtils::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 将set进行展开
     * @param lists
     * @param mapper
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T, R> Set<R> flatMap(Set<T> lists, Function<T, Collection<R>> mapper) {
        return lists.stream().map(mapper).filter(CollectionUtils::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    /**
     * 对list进行分组
     * @param lists
     * @param classifier
     * @return
     * @param <T>
     * @param <K>
     */
    public static <T, K> Map<K, List<T>> grouping(Collection<T> lists, Function<T, K> classifier) {
        return grouping(lists, classifier, Function.identity());
    }

    /**
     * 对list进行分组
     * @param lists
     * @param classifier
     * @param mapper
     * @return
     * @param <T>
     * @param <K>
     * @param <E>
     */
    public static <T, K, E> Map<K, List<E>> grouping(Collection<T> lists, Function<T, K> classifier, Function<T, E> mapper) {
        return lists.stream().collect(Collectors.groupingBy(classifier, Collectors.mapping(mapper, Collectors.toList())));
    }

    /**
     * 对list进行分组，并对分组后的list转成map
     * @param lists 数组
     * @param groupKey 分组key获取
     * @param rekeyMapper 分组后重复数据映射key获取
     * @return
     * @param <T>
     * @param <K1>
     * @param <K2>
     */
    public static <T, K1, K2> Map<K1, Map<K2, T>> multiGrouping(Collection<T> lists, Function<T, K1> groupKey, Function<T, K2> rekeyMapper) {
        return lists.stream().collect(
                Collectors.groupingBy(groupKey, Collectors.toMap(rekeyMapper, Function.identity(), (oldVal, newVal) -> newVal))
        );
    }

    /**
     * 对list进行分组，对分组后的list进行映射后，转成map
     * @param lists 数组
     * @param groupKey 分组key获取
     * @param rekeyMapper 分组后重复数据映射key获取
     * @param resValueMapper 最终结果值mapper
     * @return
     * @param <T>
     * @param <K1>
     * @param <K2>
     * @param <V1>
     */
    public static <T, K1, K2, V1> Map<K1, Map<K2, V1>> multiGrouping(Collection<T> lists, Function<T, K1> groupKey, Function<T, K2> rekeyMapper, Function<T, V1> resValueMapper) {
        return lists.stream().collect(
                Collectors.groupingBy(groupKey, Collectors.toMap(rekeyMapper, resValueMapper, (oldVal, newVal) -> newVal))
        );
    }

    /**
     * 将数组切割后转成list
     * @param str
     * @param regex
     * @return
     */
    public static List<String> toList(String str, String regex) {
        return Arrays.stream(str.split(regex)).collect(Collectors.toList());
    }

    /**
     * 将list进行map操作
     * @param lists
     * @param mapper
     * @return
     * @param <T>
     * @param <K>
     */
    public static <T, K> List<K> toList(Collection<T> lists, Function<T, K> mapper) {
        return lists.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 将list进行map操作后，进行过滤
     * @param lists
     * @param mapper
     * @param predicate
     * @return
     * @param <T>
     * @param <K>
     */
    public static <T, K> List<K> toList(Collection<T> lists, Function<T, K> mapper, Predicate<K> predicate) {
        return lists.stream().map(mapper).filter(predicate).collect(Collectors.toList());
    }

    /**
     * 将数组切割后转成set
     * @param str
     * @param regex
     * @return
     */
    public static Set<String> toSet(String str, String regex) {
        return Arrays.stream(str.split(regex)).collect(Collectors.toSet());
    }

    /**
     * 将集合进行map后转set
     * @param lists
     * @param mapper
     * @return
     * @param <T>
     * @param <K>
     */
    public static <T, K> Set<K> toSet(Collection<T> lists, Function<T, K> mapper) {
        return lists.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * 将集合map后进行过滤，后转成set
     * @param lists 集合
     * @param mapper 转成set
     * @param predicate 过滤条件
     * @return
     * @param <T>
     * @param <K>
     */
    public static <T, K> Set<K> toSet(Collection<T> lists, Function<T, K> mapper, Predicate<K> predicate) {
        return lists.stream().map(mapper).filter(predicate).collect(Collectors.toSet());
    }

    /**
     * 将集合转成map
     * @param lists 集合
     * @param keyMapper key
     * @return
     * @param <T> 原类型
     * @param <K> key类型
     */
    public static <T, K> Map<K, T> toMap(Collection<T> lists, Function<T, K> keyMapper) {
        return toMap(lists, keyMapper, Function.identity());
    }

    /**
     * 将list转成map
     * @param lists 集合
     * @param keyMapper key
     * @param valueMapper value
     * @return
     * @param <T> 原类型
     * @param <K> key类型
     * @param <V> value类型
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> lists, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return toMap(lists, Function.identity(), keyMapper, valueMapper);
    }

    /**
     * 将list采用指定map映射后，在按指定key、value映射方式转成map
     * @param lists 集合
     * @param mapper 将集合map操作
     * @param keyMapper key值
     * @param valueMapper value值
     * @return
     * @param <T> 原类型
     * @param <R> map后的类型
     * @param <K> key类型
     * @param <V> value类型
     */
    public static <T, R, K, V> Map<K, V> toMap(Collection<T> lists, Function<T, R> mapper, Function<R, K> keyMapper, Function<R, V> valueMapper) {
        return lists.stream().map(mapper).collect(
                Collectors.toMap(keyMapper, valueMapper, (oldVal, newVal) -> newVal)
        );
    }

    /**
     * 将迭代器转成map
     * @param iterable 迭代器
     * @param keyMapper key值获取方式
     * @return
     * @param <T> 原类型
     * @param <K> key类型
     */
    public static <T, K> Map<K, T> toMap(Iterable<T> iterable, Function<T, K> keyMapper) {
        Collection<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return toMap(list, keyMapper);
    }

    /**
     * 使用指定comparator进行去重
     * @param lists
     * @param comparator
     * @return
     * @param <T>
     */
    public static <T> List<T> distinct(Collection<T> lists, Comparator<T> comparator) {
        return lists.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<T>(comparator)), ArrayList<T>::new)
        );
    }

    /**
     * 简单去重
     * @param lists
     * @return
     * @param <T>
     */
    public static <T> List<T> distinct(Collection<T> lists) {
        return lists.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 使用指定comparator进行排序
     */
    public static <T> List<T> sorted(List<T> lists, Comparator<T> comparator) {
        return lists.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * 获取第一个值
     * @param lists
     * @return
     * @param <T>
     */
    public static <T> T first(Collection<T> lists) {
        return lists.stream().findFirst().orElse(null);
    }

    /**
     * 获取第一个值，如果值不为空则使用mapping进行处理
     * @param lists
     * @param mapping
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T, R> R first(Collection<T> lists, Function<T, R> mapping) {
        return lists.stream().map(mapping).findFirst().orElse(null);
    }

    /**
     * 值比较
     * @param keyExtractor 获取值的方式
     * @param supplier 如果值为空的补偿
     * @return
     * @param <T>
     * @param <U>
     */
    public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor, Supplier<? extends U> supplier) {
        return ((c1, c2) -> {
            U apply = keyExtractor.apply(c1);
            return Optional.ofNullable(apply).orElse(supplier.get()).compareTo(keyExtractor.apply(c2));
        });
    }
}
