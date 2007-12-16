/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */

package org.ucam.semantilog.simplevocab.gui;

import org.ucam.semantilog.simplevocab.VocabTrainer;
import org.ucam.semantilog.simplevocab.gui.VocabGUIFrame;



public class VocabGUIMain
  {
    static VocabGUIFrame frame;
    static VocabTrainer vocabTrainer;
    
    private static void createAndShowGUI()
      {
        frame.setDefaultCloseOperation( VocabGUIFrame.EXIT_ON_CLOSE );
        frame.setSize( 800, 600 );
        frame.setVisible( true );
      }

    public static void main(String[] args)
      {
        javax.swing.SwingUtilities.invokeLater(
          new Runnable()
            {
              public void run()
                {
                  frame = new VocabGUIFrame();
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
