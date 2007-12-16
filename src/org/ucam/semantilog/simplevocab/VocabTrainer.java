/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */

package org.ucam.semantilog.simplevocab;

import java.io.*;
import java.util.*;

import java.nio.charset.*;



public class VocabTrainer implements Serializable
  {
    static final long serialVersionUID = 0;

    public static final Random random = new Random();
    
    Vector deck;
    HashSet allChars;
    SRCard current;

    int completed;

    String message = null;
    
    int status = 0;
    
    public static final int STATUS_UNINITIALIZED = -1;
    public static final int STATUS_INITIALIZED = -2;
    public static final int STATUS_FINISHED = -3;

    public static final int STATUS_VOCAB = 1;
    public static final int STATUS_FAILED = 2;
    public static final int STATUS_SUCCESS = 3;
    
    public VocabTrainer()
      {
        message = "Simple Vocab";
        deck = new Vector();
        allChars = new HashSet();
        status = STATUS_UNINITIALIZED;
      }
    
    public void loadVocab( String filename, boolean fleft, boolean fright, boolean reverse ) throws FileNotFoundException, IOException 
      {
        BufferedReader reader = new BufferedReader(
          new InputStreamReader(
            new FileInputStream( filename ),
            Charset.forName( Config.getInstance().getInputEncoding() )
          )
        );
        
        String line = reader.readLine();

        while ( line != null )
          {
            StringTokenizer tok = new StringTokenizer( line, ":" );
            String left = tok.nextToken();
            String right = tok.nextToken();

            if (fleft)
              left = left.replaceAll( ";", "," );
            if (fright)
              right = right.replaceAll( ";", "," );

            String stimulus = left.trim();
            String response = right.trim();
            StringTokenizer stimTok = new StringTokenizer( stimulus, ";" );
            String nextTok;
            
            while ( stimTok.hasMoreTokens() )
              {
                nextTok = stimTok.nextToken().trim();
                if ( ! ( nextTok.startsWith("(") && ( nextTok.endsWith(")") ) ) )
                  deck.addElement( new SRCard( nextTok, response ) );
                // System.out.println( nextTok + ":" + response );
              }
            
            if ( reverse )
              {
                stimulus = right;
                response = left;
                stimTok = new StringTokenizer( stimulus, ";" );
                while (stimTok.hasMoreTokens())
                  {
                    nextTok = stimTok.nextToken().trim();
                    if ( ! ( nextTok.startsWith("(") && ( nextTok.endsWith(")") ) ) )
                      deck.addElement( new SRCard( nextTok, response ) );
                    // System.out.println( nextTok + ":" + response );
                  }
              }
            
            for ( int i=0; i<line.length(); i++ )
              {
                // int j = line.codePointAt( i );
                int j = ( int )( line.charAt(i) );
                if ( j > 0x7F )
                  allChars.add( new Character( line.charAt( i ) ) );
              }
            
            line = reader.readLine();
          }
        
        completed = 0;
        
        status = STATUS_INITIALIZED;
      }
    
    public HashSet getCharacters( int limit )
      {
        HashSet set;
        if ( current != null )
          set = current.getCharacters();
        else
          set = new HashSet();

        Vector chars = new Vector( allChars );
        Random rnd = new Random();
        
        while ( ( set.size() < limit ) && ( chars.size() > 0 ) ) 
          {
            int i = rnd.nextInt( chars.size() );
            Character chr = ( Character )chars.get( i );
            set.add( chr );
            chars.remove( i );
          }
        
        /*
        while ( set.size() < limit )
          {
            int i = rnd.nextInt( 0x10000-0x7F ) + 0x7F;
            Character chr = new Character( (char)i );
            set.add( chr );
          }
        */
        
        return set;
      }

    public int getCompleted()
      {
        return completed;
      }
    
    public int getRemaining()
      {
        return deck.size();
      }

    public int getStatus()
      {
        return status;
      }
    
    public String getMessage()
      {
        return message;
      }
    
    public String getStimMsg()
      {
        if ( current != null )
          return current.getStimMsg();
        return "";
      }

    public String getRespMsg()
      {
        if ( current != null )
          return current.getRespMsg();
        return "";
      }

    public void respond( String response )
      {
        if (current == null)
          return;
        
        current.respond( response );
        
        message = "";
        
        if ( current.getStatus() == SRCard.STATUS_SUCCESS )
          {
            deck.remove( current );
            completed += 1;
            message = "Good!";
            status = STATUS_SUCCESS;
          }
        else if ( current.getStatus() == SRCard.STATUS_FAILED )
          {
            deck.add( new SRCard( current ) );
            message = "No.";
            status = STATUS_FAILED;
          }
        else if ( current.getStatus() == SRCard.STATUS_BASE )
          message = "Okay...";
      }
    
    public void reset()
      {
        if ( current != null )
          current.reset();
      }
    
    public void next()
      {
        if (deck.size() == 0)
          {
            message = "Finished!";
            current = null;
            status = STATUS_FINISHED;
          }
        else
          {
            int i = (int)( random.nextFloat() * (float)deck.size() );
            current = (SRCard)deck.elementAt( i );
            message = "Please translate.";
            status = STATUS_VOCAB;
          }
      }

  }

/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */
