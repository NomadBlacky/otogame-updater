package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_user_data

import org.nomadblacky.otogameupdater.game.cbrev.model.UserData
import org.scalatest.{FunSuite, Matchers}
import awscala.DateTime

class UserDataRecordTest extends FunSuite with Matchers {

  private val userData = UserData("hoge", 1234.56, 5, 9876)
  val record = UserDataRecord(userData, DateTime.parse("2017-1-1"))

  test("testUserData") {
    record.userData shouldBe userData
  }

  test("testKey") {
    record.key shouldBe 9876
  }

  test("testAttributes") {
    val expect = Seq(
      "name" -> "hoge",
      "rankPoint" -> 1234.56,
      "charangeClass" -> 5,
      "updated" -> DateTime.parse("2017-1-1")
    )
    record.attributes shouldBe expect
  }

  test("testUpdated") {
    record.updated shouldBe DateTime.parse("2017-1-1")
  }

}
