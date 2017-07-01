package com.danielwestheide.kontextfrei.syntax

import com.danielwestheide.kontextfrei.DCollectionOps
import org.apache.spark.Partitioner
import org.apache.spark.storage.StorageLevel

import scala.collection.Map
import scala.reflect.ClassTag

class BaseSyntax[DCollection[_], A: ClassTag](
    val self: DCollectionOps[DCollection],
    val coll: DCollection[A]) {

  final def cartesian[B: ClassTag](bs: DCollection[B]): DCollection[(A, B)] =
    self.cartesian(coll)(bs)

  final def collect[B: ClassTag](pf: PartialFunction[A, B]): DCollection[B] =
    self.collect(coll)(pf)

  final def distinct(): DCollection[A] = self.distinct(coll)

  final def distinct(numPartitions: Int): DCollection[A] =
    self.distinctWithNumPartitions(coll)(numPartitions)

  final def map[B: ClassTag](f: A => B): DCollection[B] = self.map(coll)(f)

  final def flatMap[B: ClassTag](f: A => TraversableOnce[B]): DCollection[B] =
    self.flatMap(coll)(f)
  final def filter(f: A => Boolean): DCollection[A] = self.filter(coll)(f)

  final def groupBy[B: ClassTag](f: A => B): DCollection[(B, Iterable[A])] =
    self.groupBy(coll)(f)

  final def groupBy[B: ClassTag](
      f: A => B,
      numPartitions: Int): DCollection[(B, Iterable[A])] =
    self.groupByWithNumPartitions(coll)(f, numPartitions)

  final def groupBy[B: ClassTag](
      f: A => B,
      partitioner: Partitioner): DCollection[(B, Iterable[A])] =
    self.groupByWithPartitioner(coll)(f, partitioner)

  final def mapPartitions[B: ClassTag](
      f: Iterator[A] => Iterator[B],
      preservesPartitioning: Boolean = false): DCollection[B] =
    self.mapPartitions(coll)(f, preservesPartitioning)

  final def keyBy[B](f: A => B): DCollection[(B, A)] = self.keyBy(coll)(f)

  final def union(other: DCollection[A]): DCollection[A] =
    self.union(coll)(other)

  final def ++(other: DCollection[A]): DCollection[A] = self.union(coll)(other)

  final def intersection(other: DCollection[A]): DCollection[A] =
    self.intersection(coll)(other)

  final def intersection(other: DCollection[A],
                         partitioner: Partitioner): DCollection[A] =
    self.intersectionWithPartitioner(coll)(other, partitioner)

  final def intersection(other: DCollection[A],
                         numPartitions: Int): DCollection[A] =
    self.intersectionWithNumPartitions(coll)(other, numPartitions)

  final def zip[B: ClassTag](other: DCollection[B]): DCollection[(A, B)] =
    self.zip(coll)(other)

  final def subtract(other: DCollection[A]): DCollection[A] =
    self.subtract(coll)(other)

  final def subtract(other: DCollection[A],
                     numPartitions: Int): DCollection[A] =
    self.subtractWithNumPartitions(coll)(other, numPartitions)

  final def subtract(other: DCollection[A],
                     partititioner: Partitioner): DCollection[A] =
    self.subtractWithPartitioner(coll)(other, partititioner)

  final def persist(): DCollection[A] = self.persist(coll)

  final def persist(newLevel: StorageLevel): DCollection[A] =
    self.persistWithStorageLevel(coll)(newLevel)

  final def unpersist(blocking: Boolean = true): DCollection[A] =
    self.unpersist(coll)(blocking)

  final def glom(): DCollection[Array[A]] = self.glom(coll)

  final def sortBy[B: ClassTag: Ordering](
      f: A => B,
      ascending: Boolean = true): DCollection[A] =
    self.sortBy(coll)(f)(ascending)

  final def sortBy[B: ClassTag: Ordering](f: A => B,
                                          ascending: Boolean,
                                          numPartitions: Int): DCollection[A] =
    self.sortByWithNumPartitions(coll)(f)(ascending)(numPartitions)

  final def collect(): Array[A] = self.collectAsArray(coll)

  final def count(): Long = self.count(coll)

  final def countByValue()(implicit ord: Ordering[A]): Map[A, Long] =
    self.countByValue(coll)

  final def first(): A = self.first(coll)

  final def take(num: Int): Array[A] = self.take(coll)(num)

  final def takeOrdered(num: Int)(implicit ord: Ordering[A]): Array[A] =
    self.takeOrdered(coll)(num)

  final def top(num: Int)(implicit ord: Ordering[A]): Array[A] =
    self.top(coll)(num)

  final def repartition(numPartitions: Int): DCollection[A] =
    self.repartition(coll)(numPartitions)

  final def coalesce(numPartitions: Int,
                     shuffle: Boolean = false): DCollection[A] =
    self.coalesce(coll)(numPartitions, shuffle)

}