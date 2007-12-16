/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */

package org.ucam.semantilog.simplevocab;
import javax.swing.*;        

public class HelloWorld
{

    private static void createAndShowGUI()
      {
        JFrame frame = new JFrame( "HelloWorldSwing" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JLabel label = new JLabel( "Hello World" );
        frame.getContentPane().add( label );
        frame.pack();
        frame.setVisible( true );
      }

    public static void main(String[] args)
      {
        javax.swing.SwingUtilities.invokeLater(
          new Runnable()
            {
              public void run()
                {
                  createAndShowGUI();
                }
            }
        );
    }

}

/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */
