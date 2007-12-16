/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */

package org.ucam.semantilog.simplevocab.gui;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import org.ucam.semantilog.simplevocab.*;

import java.io.*;
import java.util.*;


public class VocabGUIFrame extends JFrame implements ActionListener
{
  static final long serialVersionUID = 0;
  
  VocabTrainer vocabTrainer;
  
  JPanel superPane;
  JPanel mainPane;
  
  VocabGUICharPalette charPalette;

  JTextArea stimMsgTxt;
  JTextArea respMsgTxt;
  JLabel messageLbl;
  
  JTextField mainEdt;
  JButton mainBtn;

  JLabel statusLbl;
  JProgressBar progressBar;
  
  public VocabGUIFrame()
    {
      super( "SimpleVocab" );
      
      this.vocabTrainer = null;
      
      superPane = new JPanel( new BorderLayout( 10, 10 ) );
      mainPane = new JPanel( new BorderLayout( 10, 10 ) );
      charPalette = new VocabGUICharPalette( this );
      superPane.add( mainPane, BorderLayout.CENTER );
      superPane.add( charPalette, BorderLayout.SOUTH );
      
      superPane.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
      
      stimMsgTxt = new JTextArea();
      respMsgTxt = new JTextArea();
      messageLbl = new JLabel();
      
      mainEdt = new JTextField( "" );
      mainBtn = new JButton( "OK" );

      statusLbl = new JLabel( "0/0" );
      progressBar = new JProgressBar( 0, 1 );
      
      Font dspfnt = Font.decode( Config.getInstance().getGUIFont() );
      // Font dspfnt = messageLbl.getFont();

      stimMsgTxt.setFont( dspfnt );
      respMsgTxt.setFont( dspfnt );
      messageLbl.setFont( dspfnt.deriveFont( (float)( dspfnt.getSize()*0.75 ) ) );
      
      mainEdt.setFont( dspfnt );
      mainBtn.setFont( dspfnt.deriveFont( (float)( dspfnt.getSize()*0.75 ) ) );
      
      stimMsgTxt.setEditable( false );
      stimMsgTxt.setBackground( messageLbl.getBackground() );
      stimMsgTxt.setSelectionColor( messageLbl.getBackground() );
      respMsgTxt.setEditable( false );
      respMsgTxt.setBackground( messageLbl.getBackground() );
      respMsgTxt.setSelectionColor( messageLbl.getBackground() );
      charPalette.setBackground( messageLbl.getBackground() );
      
      mainEdt.setActionCommand( "" );
      mainEdt.addActionListener( this );
      mainBtn.setActionCommand( "" );
      mainBtn.addActionListener( this );
      
      JPanel centerPane = new JPanel( new BorderLayout(10,10) );
      mainPane.add( centerPane, BorderLayout.CENTER );
      
      JPanel centerCenterPane = new JPanel( new BorderLayout(10,10) );
      centerPane.add( centerCenterPane, BorderLayout.CENTER );
      centerCenterPane.add( messageLbl, BorderLayout.NORTH );
      JPanel centerCenterCenterPane = new JPanel( new GridLayout(1,2) );
      centerCenterCenterPane.add( stimMsgTxt );
      centerCenterCenterPane.add( respMsgTxt );
      centerCenterPane.add( centerCenterCenterPane, BorderLayout.CENTER );

      messageLbl.setHorizontalAlignment( SwingConstants.RIGHT );
      
      centerPane.add( mainEdt, BorderLayout.SOUTH );
      
      JPanel southPane = new JPanel( new BorderLayout( 10, 10 ) );
      mainPane.add( southPane, BorderLayout.SOUTH );
      southPane.add( mainBtn, BorderLayout.EAST );
      southPane.add( statusLbl, BorderLayout.WEST );
      southPane.add( progressBar, BorderLayout.CENTER );
      
      JMenuBar menuBar = new JMenuBar();
      
      JMenu menu1 = new JMenu( "New Session" );
      menu1.setMnemonic( KeyEvent.VK_N );
      menuBar.add( menu1 );
      
      File dir = new File( "data" );
      String[] files = dir.list();

      if (files != null)
        for (int i=0; i<files.length; i++)
          {
            String filename = files[i];
            if ( filename.endsWith( ".tsd" ) )
              {
                JMenuItem newitem = new JMenuItem( filename );
                newitem.setActionCommand( "n"+filename );
                newitem.addActionListener( this );
                menu1.add( newitem );
              }
          }

      JMenu menu2 = new JMenu( "Save Session" );
      menu2.setMnemonic( KeyEvent.VK_S );
      menuBar.add( menu2 );

      JMenu menu3 = new JMenu( "Load Session" );
      menu3.setMnemonic( KeyEvent.VK_L );
      menuBar.add( menu3 );
      
      dir = new File( "data/sessions" );
      files = dir.list();

      if (files != null)
        for (int i=0; i<files.length; i++)
          {
            String filename = files[i];
            if ( filename.endsWith( ".ses" ) )
              {
                JMenuItem newitem = new JMenuItem( filename );
                newitem.setActionCommand( "s"+filename );
                newitem.addActionListener( this );
                menu2.add( newitem );
                newitem = new JMenuItem( filename );
                newitem.setActionCommand( "l"+filename );
                newitem.addActionListener( this );
                menu3.add( newitem );
              }
          }
      
      setJMenuBar( menuBar );
      getContentPane().add( superPane );
      
      refresh();
    }
  
  public void appendChar( Character ch )
    {
      mainEdt.setText( mainEdt.getText() + ch );
    }
  
  public void actionPerformed( ActionEvent evt )
    {
      String cmd = evt.getActionCommand();
      System.out.println( cmd );
      if ( cmd.equals("") )
        {
          if ( vocabTrainer != null )
            {
              if ( vocabTrainer.getStatus() == VocabTrainer.STATUS_FAILED )
                {
                  vocabTrainer.reset();
                  vocabTrainer.next();
                }
              else if ( vocabTrainer.getStatus() == VocabTrainer.STATUS_VOCAB )
                {
                  vocabTrainer.respond( mainEdt.getText() );
                  if ( vocabTrainer.getStatus() == VocabTrainer.STATUS_SUCCESS )
                    vocabTrainer.next();
                }
            }
        }
      else if ( cmd.charAt(0) == 's' )
        {
          try
            {
              if ( vocabTrainer != null )
                {
                  FileOutputStream fos = new FileOutputStream( "data/sessions/"+cmd.substring( 1 ) );
                  ObjectOutputStream oos = new ObjectOutputStream( fos );
                  oos.writeObject( vocabTrainer );
                  oos.close();
                  fos.close();
                }
            }
          catch (Exception e)
            {
              e.printStackTrace();
            }
        }
      else if ( cmd.charAt(0) == 'l' )
        {
          try
            {
              FileInputStream fis = new FileInputStream( "data/sessions/" + cmd.substring( 1 ) );
              ObjectInputStream ois = new ObjectInputStream( fis );
              vocabTrainer = ( VocabTrainer )ois.readObject();
              ois.close();
              fis.close();
            }
          catch (Exception e)
            {
              e.printStackTrace();
            }
        }
      else if ( cmd.charAt(0) == 'n' )
        {
          try
            {
              FileInputStream fis = new FileInputStream( "data/" + cmd.substring( 1 ) );
              Properties props = new Properties();
              props.load( fis );
              String vocab = props.getProperty( "vocab" );
              String sleftdown = props.getProperty( "leftdown" );
              String srightdown = props.getProperty( "rightdown" );
              String saddinvert = props.getProperty( "addinvert" );
              if ( vocab != null && sleftdown != null && srightdown != null && saddinvert != null )
                {
                  boolean leftdown = sleftdown.equals( "yes" );
                  boolean rightdown = srightdown.equals( "yes" );
                  boolean addinvert = saddinvert.equals( "yes" );
                  vocabTrainer = new VocabTrainer();
                  File f = new File( "data/"+vocab );
                  if ( !f.isDirectory() )
                    vocabTrainer.loadVocab( "data/"+vocab, leftdown, rightdown, addinvert );
                  else
                    {
                      String[] names = f.list();
                      for (int i=0; i<names.length; i++)
                        {
                          if ( names[i].endsWith( ".voc" ) )
                            vocabTrainer.loadVocab( "data/"+vocab+"/"+names[i], leftdown, rightdown, addinvert );
                        }
                    }
                }
              else
                {
                  System.err.println( "syntax error in tsd file" );
                }
              fis.close();
            }
          catch (Exception e)
            {
              e.printStackTrace();
            }
        }
      refresh();
    }
  
  public void refresh()
    {
      if ( vocabTrainer != null )
        {
          if ( vocabTrainer.getStatus() == VocabTrainer.STATUS_INITIALIZED )
            vocabTrainer.next();
          
          mainEdt.setText( "" );
          messageLbl.setText( vocabTrainer.getMessage() );
          stimMsgTxt.setText( vocabTrainer.getStimMsg() );
          respMsgTxt.setText( vocabTrainer.getRespMsg() );
          
          if ( vocabTrainer.getStatus() == VocabTrainer.STATUS_FAILED )
            respMsgTxt.setFont( respMsgTxt.getFont().deriveFont( Font.BOLD ));
          else
            respMsgTxt.setFont( respMsgTxt.getFont().deriveFont( Font.PLAIN ));
          
          int completed = vocabTrainer.getCompleted();
          int remaining = vocabTrainer.getRemaining();
          statusLbl.setText(
            (new Integer(completed)).toString() + "/" +
            (new Integer(completed+remaining)).toString()
          );
          progressBar.setMaximum( completed+remaining );
          progressBar.setValue( completed );
          
          charPalette.refresh();
        }
    }
}

/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */
