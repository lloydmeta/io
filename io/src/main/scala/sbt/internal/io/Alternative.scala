package sbt.internal.io

import sbt.io.Alternative

private[sbt] object Alternatives {
  import sbt.io.syntax._
  final def alternatives[A, B](alts: Seq[A => Option[B]]): A => Option[B] =
    alts match {
      case Seq(f, fs @ _*) => alternative(f) | alternatives(fs)
      case Seq()           => a => None
    }

  implicit def alternative[A, B](f: A => Option[B]): Alternative[A, B] =
    new Alternative[A, B] {
      def |(g: A => Option[B]) =
        (a: A) => f(a) orElse g(a)
    }
}
