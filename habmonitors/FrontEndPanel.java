package psl.habitats;
/**
 * FrontEndPanel
 *
 *
 * Created: Thu Nov 15 09:23:42 2001
 *
 * @author Christy Lauridsen
 * @version
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class FrontEndPanel extends JPanel
{
    public FrontEndPanel()
    {
	setLayout(new BorderLayout());
	add(new TextAreaPanel(), BorderLayout.CENTER);
	add(new InputPanel(), BorderLayout.SOUTH);
    }
    
    /////// Private data /////
    private JTextArea h_textArea    = new JTextArea(8, 40);
    private JButton h_execButton = new JButton("Execute");
    private JTextField h_execField  = new JTextField(20);
    
    /////// Inner classes /////
    class TextAreaPanel extends JPanel
    {
	public TextAreaPanel()
	{
	    setLayout( new BorderLayout() );
	    h_textArea = new JTextArea(8, 40);
	    h_textArea.setText(">");
	    JScrollPane scrollPane = new JScrollPane(h_textArea);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    add(scrollPane, BorderLayout.CENTER);
	}
    }
    
    class InputPanel extends JPanel
    {
	public InputPanel()
	{
	    add(new JLabel("Command:"));
	    add(h_execField);
	    add(h_execButton);
	    h_execButton.addActionListener(new ActionListener()
					      {
						  public void actionPerformed(ActionEvent ev)
						      {
							  String h_exec = h_execField.getText();
							  h_textArea.append(h_exec + "\n>");
							  h_execField.setText("");
						      }
					      }
					   );
	}
    }
} // FrontEndPanel
