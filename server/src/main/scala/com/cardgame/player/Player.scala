package com.cardgame.player

import zio.schema.Differ.currency

import java.text.NumberFormat
import java.util.{Currency, Locale}

class Player
(
  val id: String, 
  val name: String, 
  var tokens: Double, 
  var currency: Currency = Currency.getInstance(Locale.getDefault)
) {
  private val nf: NumberFormat = NumberFormat.getNumberInstance
  nf.setCurrency(currency)
  nf.setMinimumFractionDigits(2)
  nf.setMaximumFractionDigits(2)
  
  def +=(amt: Double): Unit = tokens += amt
  def -=(amt: Double): Unit = tokens -= amt
  
  def format: String = nf.format(tokens)
}
