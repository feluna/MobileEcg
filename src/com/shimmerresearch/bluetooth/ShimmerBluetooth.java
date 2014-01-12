/*
 * Copyright (c) 2010, Shimmer Research, Ltd.
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:

 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of Shimmer Research, Ltd. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Jong Chern Lim
 * @date   October, 2013
 */

package com.shimmerresearch.bluetooth;

import com.shimmerresearch.driver.ShimmerObject;

public abstract class ShimmerBluetooth extends ShimmerObject {
	/*
	 * NOTE:
	 * 
	 * The reason for having an abstract ShimmerBluetooth class, is so in the future code reuse for a PC based Shimmer Java Instrument Driver will be easy to implement. 
	 * An example of what the code will look like in the future is provided below, where core Bluetooth functionality such as connect, readbytes, and write bytes, are
	 * abstract methods to be implemented by a class inheriting it. For the moment this is not done, but there is a simple working example for use with a Windows/Linux/Mac OSx 
	 * provided in the 'Shimmer Java Instrument Driver Trial' Folder, where an example of reading the firmware version is show.
	 * 
	 */
	
	/*
	

	protected boolean mInstructionStackLock = false;
	protected byte mCurrentCommand;	
	protected boolean mWaitForAck=false;                                          // This indicates whether the device is waiting for an acknowledge packet from the Shimmer Device  
	protected boolean mWaitForResponse=false; 									// This indicates whether the device is waiting for a response packet from the Shimmer Device 
	protected boolean mTransactionCompleted=true;									// Variable is used to ensure a command has finished execution prior to executing the next command (see initialize())
	protected ConnectedThread mConnectedThread;
	protected boolean mContinousSync=false;                                       // This is to select whether to continuously check the data packets 
	protected boolean mSetupDevice=false;		
	
	protected abstract void connect(String address);
	protected abstract void readFWVersion();
	protected abstract boolean bytesToBeRead();
	protected abstract void writeBytes(byte[] data);
	protected abstract byte[] readBytes(int numberofBytes);
	protected List<byte []> mListofInstructions = new  ArrayList<byte[]>();
	
	protected class ConnectedThread extends Thread {
		byte[] tb ={0};

		public synchronized void run() {
			while (true) {
				/////////////////////////
				// is an instruction running ? if not proceed
				if (mInstructionStackLock==false){
					// check instruction stack, are there any other instructions left to be executed?
					if (!mListofInstructions.isEmpty()){
						mInstructionStackLock=true;
						byte[] insBytes = (byte[]) mListofInstructions.get(0);
						mCurrentCommand=insBytes[0];
						mWaitForAck=true;
						writeBytes(insBytes);
						if (mCurrentCommand==STOP_STREAMING_COMMAND){
							mStreaming=false;
						} else {
							// timer while waiting for ack resp etc
						}
						mTransactionCompleted=false;
					}

				}
				if (mWaitForAck==true && mStreaming ==false) {

					if (bytesToBeRead()){
						tb=readBytes(1);
					}

					if ((byte)tb[0]==ACK_COMMAND_PROCESSED)
					{	
						if (mCurrentCommand==START_STREAMING_COMMAND) {
							mStreaming=true;
							mTransactionCompleted=true;
							mWaitForAck=false;
							mListofInstructions.remove(0);
							mInstructionStackLock=false;
						}
						else if (mCurrentCommand==GET_FW_VERSION_COMMAND) {
							mWaitForResponse = true;
							mWaitForAck=false;
						}
					}


				} else if (mWaitForResponse==true) {
					if (bytesToBeRead()){
						tb=readBytes(1);
						if (tb[0]==FW_VERSION_RESPONSE){
							byte[] bufferInquiry = new byte[6]; 
							bufferInquiry=readBytes(6);
							mFWIdentifier=(double)((bufferInquiry[1]&0xFF)<<8)+(double)(bufferInquiry[0]&0xFF);
							mFWVersion=(double)((bufferInquiry[3]&0xFF)<<8)+(double)(bufferInquiry[2]&0xFF)+((double)((bufferInquiry[4]&0xFF))/10);
							mFWInternal=(int)(bufferInquiry[5]&0xFF);
							if (((double)((bufferInquiry[4]&0xFF))/10)==0){
								mFWVersionFullName = "BtStream " + Double.toString(mFWVersion) + "."+ Integer.toString(mFWInternal);
							} else {
								mFWVersionFullName = "BtStream " + Double.toString(mFWVersion) + "."+ Integer.toString(mFWInternal);
							}
							System.out.print("Version:" + mFWVersionFullName);
							mListofInstructions.remove(0);
							mInstructionStackLock=false;
							mTransactionCompleted=true;
						}
					}
				}
			}
		}
	}

	
*/}
