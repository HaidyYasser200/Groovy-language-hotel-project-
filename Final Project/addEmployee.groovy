import groovy.swing.SwingBuilder
import javax.swing.*
import java.awt.*
import java.awt.BorderLayout as BL
import static javax.swing.JFrame.EXIT_ON_CLOSE
import static java.awt.GridBagConstraints.EAST
import static java.awt.GridBagConstraints.REMAINDER
import static javax.swing.SwingConstants.HORIZONTAL

def addEmployeeFrame(swing,frameLogin,frameHome){
        GroovyShell shell3 = new GroovyShell()
        Class addClass=shell3.getClassLoader().parseClass(new File('login.groovy'))
        GroovyObject tools3 = (GroovyObject) addClass.newInstance()
        swing = new SwingBuilder()
        def labelfont =new Font("Lora", Font.PLAIN, 17)
        def backgroundColor =new Color(16, 37, 71);
        def defaultInsets = [10,10,10,10]
        String itemSelected=''
        def size=[700, 500]
        return swing.frame(id:'addempView',title: 'Add Employee Form',location : [500, 200], size: size, 
        defaultCloseOperation: EXIT_ON_CLOSE,show:true) {
                menuBar {
                        menuItem('Back',actionPerformed:{
                                frameHome.visible=true
                                addempView.dispose()
                        })
                        menuItem()
                        menuItem()
                        menuItem()
                        menuItem() 
                        menuItem('Sign Out',actionPerformed:{
                                frameLogin.visible=true
                                addempView.dispose()
                        })
                }
                scrollPane{
                        panel(background:backgroundColor,border: titledBorder(font:new Font("Lora", Font.BOLD, 15),color:Color.WHITE,title: 'Add Employee page', justification: 'leading', position: 'Top')){
                                borderLayout()
                        
                                panel(background:backgroundColor){
                                        gridBagLayout()
                                        label(
                                                foreground:Color.WHITE,
                                                font:labelfont,
                                                text: "Name :",
                                                constraints: gbc(gridx:0, gridy:0, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                                        )
                                        textField(
                                                font:labelfont,
                                                id: "nameField1",
                                                preferredSize: new Dimension(220,28),
                                                constraints: gbc(gridx:1, gridy:0, gridwidth:REMAINDER, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                                        )
                                        label(
                                                foreground:Color.WHITE,
                                                font:labelfont,
                                                text: "Email :",
                                                constraints: gbc(gridx:0, gridy:1, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                                        )
                                        textField(
                                                font:labelfont,
                                                id: "emailField1",
                                                preferredSize: new Dimension(220,28),
                                                constraints: gbc(gridx:1, gridy:1, gridwidth:REMAINDER, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                                        )
                                        label(
                                                foreground:Color.WHITE,
                                                font:labelfont,
                                                text: "Employment :",
                                                constraints: gbc(gridx:0, gridy:2, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                                        )
                                        buttonGroup(id:"radioButton").with { group ->
                                                                        [0: 'Admin', 1: 'Receptionist'].each { e ->
                                                                                radioButton(
                                                                                        foreground:Color.WHITE,
                                                                                        background:backgroundColor,
                                                                                        font:labelfont,
                                                                                        actionPerformed: {
                                                                                                itemSelected=e.value
                                                                                        },
                                                                                        clientPropertyId: e.key, 
                                                                                        text: e.value, 
                                                                                        buttonGroup: group,
                                                                                        constraints:gbc("hidemode 3") 
                                                                                )
                                                                        }
                                                                }
                                        label(
                                                foreground:Color.WHITE,
                                                font:labelfont,
                                                text: "Phone number :",
                                                constraints: gbc(gridx:0, gridy:3, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                                        )
                                        textField(
                                                font:labelfont,
                                                id: "phoneField1",
                                                preferredSize: new Dimension(220,40),
                                                constraints: gbc(gridx:1, gridy:3, gridwidth:REMAINDER, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                                        )
                                        label(
                                                foreground:Color.WHITE,
                                                font:labelfont,
                                                text:"Password :",
                                                constraints: gbc(gridx:0, gridy:4, fill:HORIZONTAL, insets:defaultInsets, anchor: WEST)
                                        )
                                        passwordField(
                                                font:labelfont,
                                                id:"passwordField1",
                                                preferredSize: new Dimension(220,28),
                                                constraints: gbc(gridx:1, gridy:4, fill:HORIZONTAL, insets: defaultInsets, anchor: WEST)
                                        )
                                        button(
                                                font:labelfont,
                                                text:'Add',
                                                constraints:gbc(gridx:3,gridy:5,insets:defaultInsets,gridwidth:3, anchor: EAST),
                                                actionPerformed:{
                                                        def cn=tools3.checkName(nameField1.text)
                                                        def ct=tools3.checkTelephone(phoneField1.text)
                                                        def ce=tools3.checkEmail(emailField1.text)
                                                        def cp=tools3.checkPassword(passwordField1.text)
                                                        def ci=tools3.checkEmployment(itemSelected)
                                                        if(!cn && !ct && !ce && !cp && !ci){
                                                                String y=tools3.addEmploymee(swing.nameField1.text,swing.emailField1.text,itemSelected,swing.phoneField1.text,swing.passwordField1.text)
                                                                // JOptionPane.showMessageDialog(null, y, "Message", JOptionPane.INFORMATION_MESSAGE);
                                                                swing.nameField1.text=''
                                                                swing.emailField1.text=''
                                                                swing.phoneField1.text=''
                                                                swing.passwordField1.text=''
                                                                swing.radioButton.clearSelection()
                                                                snackbarPanel.visible=true
                                                                snackbar.text=show([y])
                                                        }
                                                        else {
                                                                snackbarPanel.visible=true
                                                                snackbar.text=show([cn,ct,ce,cp,ci])
                                                        }
                                                }
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