import groovy.swing.SwingBuilder
import java.awt.Color
import java.awt.BorderLayout as BL
import java.awt.*
import javax.swing.BorderFactory
import static javax.swing.JFrame.EXIT_ON_CLOSE
import javax.swing.JOptionPane;

def guiCheckOut(swing,frameLogin,frameHome){
    GroovyShell shell = new GroovyShell()
    Class clientClass=shell.getClassLoader().parseClass(new File('client.groovy'))
    GroovyObject client = (GroovyObject) clientClass.newInstance()
    def defaultInsets = [0,12,12,0]
    def labelfont =new Font("Lora", Font.PLAIN, 17)
    def backgroundColor =new Color(16, 37, 71)
    return swing.frame(id:'checkoutv',title: 'Hotel',location : [500, 200], size: [700, 500],
    defaultCloseOperation: EXIT_ON_CLOSE,show: true
    ) {
        menuBar (){
            menuItem('Back',actionPerformed:{
                frameHome.visible=true
                checkoutv.dispose()
            })
            menuItem()
            menuItem()
            menuItem()
            menuItem()
            menuItem('Sign Out',actionPerformed:{
                frameLogin.visible=true
                checkoutv.dispose()
            })
        }
        
        panel(background:backgroundColor,border: titledBorder(font:new Font("Lora", Font.BOLD, 15),color:Color.WHITE,title: 'Check out page', justification: 'leading', position: 'Top')){
            borderLayout()
            panel(background:backgroundColor,constraints: BL.CENTER){
                gridBagLayout()   
                label (text:'Name:',font:labelfont,constraints: gbc(gridx:0, gridy:1, fill:HORIZONTAL, insets:defaultInsets),foreground:Color.WHITE)
                textField(text:'',font:labelfont,constraints: gbc(gridx:1, gridy:1, fill:HORIZONTAL, insets:defaultInsets),id:'nameC',columns:20)

                label (text:'Number Of Room:',font:labelfont,constraints: gbc(gridx:0, gridy:3, fill:HORIZONTAL, insets:defaultInsets),foreground:Color.WHITE)
                textField(text:'',font:labelfont,constraints: gbc(gridx:1, gridy:3, fill:HORIZONTAL, insets:defaultInsets),id:'numRoomC',columns:20)
                button(
                    font:labelfont,
                    constraints:gbc(gridx:0,gridy:5,insets:[10,0,0,0],gridwidth:3, anchor: EAST),
                    text: 'Check out',
                    actionPerformed: {
                        def cn=client.checkName(nameC.text)
                        def cre=client.checkRoomExist(numRoomC.text,nameC.text)
                        if(!cn && !cre){
                            price = client.priceCal(nameC.text,numRoomC.text)
                            pr.text= "Check-out bill:${price}"
                            pr.visible=true
                            snackbarPanel.visible=true
                            snackbar.text=show(['Successfully Check Out'])
                        }
                        else {
                            pr.visible=false
                            snackbarPanel.visible=true
                            snackbar.text=show([cn,cre])
                        }
                        
                    }
                )
                label (
                    constraints: gbc(gridx:0, gridy:7, fill:HORIZONTAL, insets:defaultInsets),
                    id:'pr',
                    text:'',
                    font:new Font("Lora", Font.BOLD, 17),
                    foreground:Color.WHITE
                )

            }
            panel(visible:false,background:Color.BLACK,constraints:BL.SOUTH,id:'snackbarPanel'){
            textArea(text:"",margin:[0,4,0,4],background:Color.BLACK,id:'snackbar',editable:false,foreground:Color.WHITE,font:labelfont,columns:40)
            button(
                border: etchedBorder(),
                background:Color.BLACK,
                foreground:Color.WHITE,
                font:new Font("Lora", Font.PLAIN, 15),
                text: 'X',
                defaultCapable:false,
                actionPerformed: {
                    snackbarPanel.visible=false
                }
            )
            }
        }
        
    }
}
def show(checkArr){

    def content='\n'
    for(def i in checkArr) { 
        if(i!=0){
            content+=i+" \n"
        }
    }
    return content
     
}