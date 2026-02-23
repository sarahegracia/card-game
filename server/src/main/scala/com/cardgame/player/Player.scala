package com.cardgame.player

import zio.schema.Differ.currency

import java.text.NumberFormat
import java.util.{Currency, Locale}

class Player(val name: String, var tokens: Double, var currency: Currency = Currency.getInstance(Locale.getDefault)) {
  val nf: NumberFormat = NumberFormat.getNumberInstance
  nf.setCurrency(currency)
  nf.setMinimumFractionDigits(2)
  nf.setMaximumFractionDigits(2)
  
  def +=(amt: Double) = tokens += amt
  def -=(amt: Double) = tokens -= amt
  
  def format: String = nf.format(tokens)
}
