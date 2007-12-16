/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */

package org.ucam.semantilog.simplevocab;

import java.util.*;
import java.io.*;

public class Config
  {

    static Config inst;
    Properties props;
    
    public static Config getInstance()
      {
        if ( inst == null )
          inst = new Config();
        return inst;
      }
    
    private Config()
      {
        props = new Properties();
        try
          {
            props.load( new FileInputStream( "data/simplevocab.conf" ) );
          }
        catch (FileNotFoundException e)
          {
            System.err.println( "Warning #1" );
          }
        catch (IOException e)
          {
            System.err.println( "Warning #2" );
          }
      }
    
    public String getGUIFont()
      {
        return props.getProperty( "gui.font", "Serif-PLAIN-32" );
      }
    
    public String getInputEncoding()
      {
        return props.getProperty( "input.encoding", "UTF-8" );
      }

  }

/*
 * (c) Copyright 2007 by Richard Bergmair,
 *     See LICENSE.txt for terms and conditions
 *     for use, reproduction, and distribution.
 */