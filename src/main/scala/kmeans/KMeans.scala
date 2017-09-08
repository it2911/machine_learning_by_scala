package kmeans
import org.apache.spark.SparkContext
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors

class KMeans extends App{
  val context = new SparkContext("local", "demo")

  val data = context
              .textFile("")
              .filter(_.nonEmpty)
              .map{s =>
                val elems = s.split(",")
                (elems.last, Vectors.dense(elems.init.map(_.toDouble)))
              }

  val k = 3
  val maxItreations = 100
  val clusters = KMeans.train(data.map(_._2), k, maxItreations)

  // 各クラスタの中心を確認する
  println("## クラスタの中心")
  clusters.clusterCenters.foreach{
    center => println(f"${center.toArray.mkString("[", ", ", "]")}%s")
  }

  // 各データがどのクラスタに分類されたのかを確認する
  println("## 各データのクラスタリング結果")
  data.foreach{tuple =>
    println(f"${tuple._2.toArray.mkString("[", ", ", "]")}%s " +
      f"(${tuple._1}%s) : cluster = ${clusters.predict(tuple._2)}%d")
  }
}