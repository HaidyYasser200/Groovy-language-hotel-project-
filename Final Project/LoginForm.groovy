import groovy.swing.SwingBuilder
import javax.swing.*
import java.awt.*
import static javax.swing.JFrame.EXIT_ON_CLOSE
import static java.awt.GridBagConstraints.EAST
import static java.awt.GridBagConstraints.REMAINDER
import static javax.swing.SwingConstants.HORIZONTAL
import groovy.sql.*
import java.lang.Integer

@GrabConfig(systemClassLoader=true)
@Grab('com.oracle:ojdbc6')

GroovyShell shell = new GroovyShell()
Class loginClass=shell.getClassLoader().parseClass(new File('login.groovy'))
GroovyObject tools = (GroovyObject) loginClass.newInstance()
tools.createDatebase()
Class homePageClass=shell.getClassLoader().parseClass(new File('homePage.groovy'))
GroovyObject tools1 = (GroovyObject) homePageClass.newInstance()
def defaultInsets = [0,10,10,0]
def size=[700, 500]
def labelfont =new Font("Lora", Font.PLAIN, 17)
def backgroundColor =new Color(16, 37, 71)
swing = new SwingBuilder()
frameLogin = swing.frame(id:'loginView',title:'Hotel',location : [500, 200], size:[700, 500],
        defaultCloseOperation: EXIT_ON_CLOSE,show: true){
                panel(background:backgroundColor,border: titledBorder(font:new Font("Lora", Font.BOLD, 15),color:Color.WHITE,title: 'Log in page', justification: 'leading', position: 'Top')){
                        gridBagLayout()
                        label(
                                foreground:Color.WHITE,
                                font:labelfont,
                                text: "Email :",
                                constraints: gbc(gridx:0, gridy:0, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                        )
                        textField(
                                font:labelfont,
                                id: "emailField",
                                preferredSize: new Dimension(220,28),
                                constraints: gbc(gridx:1, gridy:0, gridwidth:REMAINDER, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                        )
                        label(
                                foreground:Color.WHITE,
                                font:labelfont,
                                text:"Password :",
                                constraints: gbc(gridx:0, gridy:1, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                        )
                        passwordField(
                                font:labelfont,
                                id:"passwordField",
                                preferredSize: new Dimension(220,28),
                                constraints: gbc(gridx:1, gridy:1, fill:HORIZONTAL, insets: defaultInsets, anchor: WEST)
                        )
                        button(
                                font:labelfont,
                                id:"button",
                                text:'Log in',
                                constraints:gbc(gridx:0,gridy:3,insets:[10,0,0,0],gridwidth:3, anchor: EAST,),
                                actionPerformed:{
                                       def x= tools.login(swing.emailField.text,swing.passwordField.text)
                                        if (x!="Incorrect email or password" && x!="Field Login"){
                                                tools1.homeFrame(x,swing,frameLogin)
                                                loginView.dispose()
                                        }else{ 
                                                tools1.homeFrame(x,swing,frameLogin)
                                        }
                                }
                        )
                }
        }
