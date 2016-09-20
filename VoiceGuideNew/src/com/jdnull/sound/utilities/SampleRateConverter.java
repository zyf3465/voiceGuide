/*
 *	SampleRateConverter.java
 *
 *	This file is part of jsresources.org
 */

/*
 * Copyright (c) 1999 - 2003 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/
package com.jdnull.sound.utilities;

import java.io.IOException;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;



/**	<titleabbrev>SampleRateConverter</titleabbrev>
 <title>Converting the sample rate of audio files</title>

 <formalpara><title>Purpose</title>
 <para>Converts audio files, changing the sample rate of the
 audio data.</para>
 </formalpara>

 <formalpara><title>Usage</title>
 <para>
 <cmdsynopsis>
 <command>java SampleRateConverter</command>
 <arg choice="plain"><option>-h</option></arg>
 </cmdsynopsis>
 <cmdsynopsis>
 <command>java SampleRateConverter</command>
 <arg choice="plain"><replaceable class="parameter">targetsamplerate</replaceable></arg>
 <arg choice="plain"><replaceable class="parameter">sourcefile</replaceable></arg>
 <arg choice="plain"><replaceable class="parameter">targetfile</replaceable></arg>
 </cmdsynopsis>
 </para></formalpara>

 <formalpara><title>Parameters</title>
 <variablelist>
 <varlistentry>
 <term><option>-h</option></term>
 <listitem><para>prints usage information</para></listitem>
 </varlistentry>
 <varlistentry>
 <term><replaceable class="parameter">targetsamplerate</replaceable></term>
 <listitem><para>the sample rate that should be converted to</para></listitem>
 </varlistentry>
 <varlistentry>
 <term><replaceable class="parameter">sourcefile</replaceable></term>
 <listitem><para>the file name of the audio file that should be read to get the audio data to convert</para></listitem>
 </varlistentry>
 <varlistentry>
 <term><replaceable class="parameter">targetfile</replaceable></term>
 <listitem><para>the file name of the audio file that the converted audio data should be written to</para></listitem>
 </varlistentry>
 </variablelist>
 </formalpara>

 <formalpara><title>Bugs, limitations</title>
 <para>Sample rate conversion can only be handled natively
 by <ulink url="http://www.tritonus.org/">Tritonus</ulink>.
 If you want to do sample rate conversion with the
 Sun jdk1.3/1.4, you have to install Tritonus' sample rate converter.
 It is part of the 'Tritonus miscellaneous' plug-in. See <ulink url
 ="http://www.tritonus.org/plugins.html">Tritonus Plug-ins</ulink>.
 </para>
 </formalpara>

 <formalpara><title>Source code</title>
 <para>
 <ulink url="SampleRateConverter.java.html">SampleRateConverter.java</ulink>,
 <ulink url="AudioCommon.java.html">AudioCommon.java</ulink>
 </para>
 </formalpara>

 */
public class SampleRateConverter
{
    /**	Flag for debugging messages.
     *	If true, some messages are dumped to the console
     *	during operation.
     */
    private static boolean		DEBUG = true;


    public static void convert(String targetSampleRate, String sourceStr, String targetStr)
            throws UnsupportedAudioFileException, IOException
    {
        float	fTargetSampleRate = Float.parseFloat(targetSampleRate);
        if (DEBUG) { out("target sample rate: " + fTargetSampleRate); }
        File	sourceFile = new File(sourceStr);
        File	targetFile = new File(targetStr);

		/* We try to use the same audio file type for the target
		   file as the source file. So we first have to find
		   out about the source file's properties.
		*/
        AudioFileFormat		sourceFileFormat = AudioSystem.getAudioFileFormat(sourceFile);
        AudioFileFormat.Type	targetFileType = sourceFileFormat.getType();

		/* Here, we are reading the source file.
		 */
        AudioInputStream	tSourceStream = null;
        tSourceStream = AudioSystem.getAudioInputStream(sourceFile);
        if (tSourceStream == null)
        {
            out("cannot open source audio file: " + sourceFile);
            System.exit(1);
        }

        AudioFormat	sourceFormat = tSourceStream.getFormat();
//        sourceFormat = new AudioFormat(
//                sourceFormat.getEncoding(),
//                sourceFormat.getSampleRate(),
//                sourceFormat.getSampleSizeInBits(),
//                sourceFormat.getChannels(),
//                sourceFormat.getFrameSize()*2,
//                sourceFormat.getSampleRate(),
//                sourceFormat.isBigEndian());
        if (DEBUG)  { out("source format: " + sourceFormat); }

//        AudioInputStream sourceStream = AudioSystem.getAudioInputStream(sourceFormat.getEncoding(), tSourceStream);


		/* Currently, the only known and working sample rate
		   converter for Java Sound requires that the encoding
		   of the source stream is PCM (signed or unsigned).
		   So as a measure of convenience, we check if this
		   holds here.
		*/
        AudioFormat.Encoding	encoding = sourceFormat.getEncoding();
        if (! AudioCommon.isPcm(encoding))
        {
            out("encoding of source audio data is not PCM; conversion not possible");
            System.exit(1);
        }

		/* Since we now know that we are dealing with PCM, we know
		   that the frame rate is the same as the sample rate.
		*/
        float		fTargetFrameRate = fTargetSampleRate;

		/* Here, we are constructing the desired format of the
		   audio data (as the result of the conversion should be).
		   We take over all values besides the sample/frame rate.
		*/

        AudioFormat	targetFormat = new AudioFormat(
                sourceFormat.getEncoding(),
                fTargetSampleRate,
                sourceFormat.getSampleSizeInBits(),
                sourceFormat.getChannels(),
                sourceFormat.getFrameSize(),
                fTargetFrameRate,
                sourceFormat.isBigEndian());

        if (DEBUG)  { out("desired target format: " + targetFormat); }

		/* Now, the conversion takes place.
		 */
        AudioInputStream	targetStream = AudioSystem.getAudioInputStream(targetFormat, tSourceStream);
        if (DEBUG) { out("targetStream: " + targetStream); }

		/* And finally, we are trying to write the converted audio
		   data to a new file.
		*/
        int	nWrittenBytes = 0;
        nWrittenBytes = AudioSystem.write(targetStream, targetFileType, targetFile);
        if (DEBUG) { out("Written bytes: " + nWrittenBytes); }
    }

    private static void out(String strMessage)
    {
        System.out.println(strMessage);
    }
}



/*** SampleRateConverter.java ***/
