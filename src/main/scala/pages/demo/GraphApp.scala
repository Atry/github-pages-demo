package pages.demo

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.document
import org.scalajs.dom.raw.{HTMLCanvasElement, HTMLElement}
import org.scalajs.{dom => jsdom}
import typings.mathjsLib.mathjsMod
import typings.plotlyDotJsLib.plotlyDotJsMod.^._
import typings.plotlyDotJsLib.plotlyDotJsMod.{Data, Layout, Margin}
import typings.stdLib.Partial
import org.scalajs.dom.raw.{Event, FileReader, HTMLElement, HTMLInputElement}

import scala.collection.immutable
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object GraphApp extends IntellijImplicits {

  val expressionVar = Var("x^2")

  @JSExportTopLevel("runJSGraph")
  def main(): Unit = {
    dom.render(document.getElementById("graphDiv"), plotly)
  }

  import scala.scalajs.js.timers.setTimeout

  @dom
  private def plotly: Binding[HTMLElement] = {
    val expression = expressionVar.bind

    setTimeout(200) {
      val expr = mathjsMod.^.compile(expression)
      val xValues = js.Array(-20 to 20: _*)
      val yValues = xValues.map { x =>
        expr.eval(
          js.Dynamic.literal(
            x = x
          )
        )
      }
      val data: js.Array[Data] = js.Array(
        js.Dynamic
          .literal(
            x = xValues,
            y = yValues
          )
          .asInstanceOf[Partial[Data]]
      )
      val margin = js.Dynamic.literal(b = 0).asInstanceOf[Partial[Margin]]
      val layout: Partial[Layout] =
        js.Dynamic
          .literal(showlegend = false)
          .asInstanceOf[Partial[Layout]]
      val elem = document.getElementById("plotDiv")
      newPlot("plotDiv", data, layout)
    }
    <form class="ui form">
      <div class="field">
        <label>Formula</label>
        <input type="text" name="formula" id="formula" placeholder="Formula" value={
      expression
    }/>
      </div>
      <button class="ui button" 
      onclick={_: Event => expressionVar.value = formula.value}>Submit</button>
    </form>
  }

}
