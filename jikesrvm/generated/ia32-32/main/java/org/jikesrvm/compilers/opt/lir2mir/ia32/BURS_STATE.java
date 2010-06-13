/*
 *  This file is part of the Jikes RVM project (http://jikesrvm.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License. You
 *  may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  See the COPYRIGHT.txt file distributed with this work for information
 *  regarding copyright ownership.
 */
package org.jikesrvm.compilers.opt.lir2mir.ia32;

import org.jikesrvm.compilers.opt.ir.ia32.*;
import org.jikesrvm.classloader.*;

import org.jikesrvm.*;
import org.jikesrvm.runtime.ArchEntrypoints;
import org.jikesrvm.compilers.opt.ir.*;
import org.jikesrvm.compilers.opt.ir.operand.*;
import org.jikesrvm.compilers.opt.ir.operand.ia32.*;
import org.jikesrvm.compilers.opt.lir2mir.BURS;
import org.jikesrvm.compilers.opt.OptimizingCompilerException;
import org.jikesrvm.compilers.opt.util.Bits; //NOPMD
import org.jikesrvm.ArchitectureSpecificOpt.BURS_TreeNode;

import org.vmmagic.unboxed.*;

/**
 * Machine-specific instruction selection rules.  Program generated.
 *
 * Note: some of the functions have been taken and modified
 * from the file gen.c, from the LCC compiler.
 * See $RVM_ROOT/rvm/src-generated/opt-burs/jburg/COPYRIGHT file for copyright restrictions.
 *
 * @see BURS
 *
 * NOTE: Program generated file, do not edit!
 */
@SuppressWarnings("unused") // Machine generated code is hard to get perfect
public class BURS_STATE extends BURS_Helpers
   implements Operators, BURS_Definitions {

          static final byte NOFLAGS           = 0x00;
          static final byte EMIT_INSTRUCTION  = 0x01;
   public static final byte LEFT_CHILD_FIRST  = 0x02;
   public static final byte RIGHT_CHILD_FIRST = 0x04;

   public BURS_STATE(BURS b) {
      super(b);
   }

/*****************************************************************/
/*                                                               */
/*  BURS TEMPLATE                                                */
/*                                                               */
/*****************************************************************/

   /* accessors used by BURS */
   private static BURS_TreeNode STATE(BURS_TreeNode a) { return a; }

   /***********************************************************************
    *
    *   This file contains BURG utilities
    *
    *   Note: some of the functions have been taken and modified
    *    from the file gen.c, from the LCC compiler.
    *
    ************************************************************************/
   void trace(BURS_TreeNode p, int rule, int cost, int bestcost) {
     if (BURS.DEBUG) {
       VM.sysWrite(p+" matched "+BURS_Debug.string[rule]+" with cost "+
		   cost+"vs. "+bestcost);
     }
   }

   /**
    * This function will dump the tree
    */
   public static void dumpTree(BURS_TreeNode p) {
     if (BURS.DEBUG) {
       VM.sysWrite(dumpTree("\n",p,1));
     }
   }

   public static String dumpTree(String out, BURS_TreeNode p, int indent) {
     if (p == null) return out;
     for (int i=0; i<indent; i++)
       out = out + "   ";
     out = out + p;
     out = out + '\n';
     if (p.child1 != null) {
       indent++;
       out = out + dumpTree("",p.child1,indent);
       if (p.child2 != null) {
	 out = out + dumpTree("",p.child2,indent);
       }
     }
     return out;
   }

   /**
    * This function will dump the cover of a tree, i.e. the rules
    * that cover the tree with a minimal cost.
    */
   public static void dumpCover(BURS_TreeNode p, byte goalnt, int indent){
      if (BURS.DEBUG) {
	if (p == null) return;
	int rule = STATE(p).rule(goalnt);
	VM.sysWrite(STATE(p).getCost(goalnt)+"\t");
	for (int i = 0; i < indent; i++)
          VM.sysWrite(' ');
	VM.sysWrite(BURS_Debug.string[rule]+"\n");
	for (int i = 0; i < nts[rule].length; i++)
          dumpCover(kids(p,rule,i), nts[rule][i], indent + 1);
      }
   }

   // caution: MARK should be used in single threaded mode,
   public static void mark(BURS_TreeNode p, byte goalnt) {
     if (p == null) return;
     int rule = STATE(p).rule(goalnt);
     byte act = action[rule];
     if ((act & EMIT_INSTRUCTION) != 0) {
       p.setNonTerminal(goalnt);
     }
     if (rule == 0) {
       throw new OptimizingCompilerException("BURS","rule missing in ",
						 p.getInstruction().toString(), dumpTree("",p,1));
     }
     mark_kids(p,rule);
   }
/*
 *  This file is part of the Jikes RVM project (http://jikesrvm.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License. You
 *  may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  See the COPYRIGHT.txt file distributed with this work for information
 *  regarding copyright ownership.
 */
//ir.brg

/**
 * Generate from ir.template and assembled rules files.
 */
 private static final byte[] nts_0 = { r_NT,  };
 private static final byte[] nts_1 = {  };
 private static final byte[] nts_2 = { czr_NT,  };
 private static final byte[] nts_3 = { szpr_NT,  };
 private static final byte[] nts_4 = { riv_NT,  };
 private static final byte[] nts_5 = { any_NT, any_NT,  };
 private static final byte[] nts_6 = { riv_NT, riv_NT,  };
 private static final byte[] nts_7 = { sload8_NT,  };
 private static final byte[] nts_8 = { uload8_NT,  };
 private static final byte[] nts_9 = { sload16_NT,  };
 private static final byte[] nts_10 = { uload16_NT,  };
 private static final byte[] nts_11 = { load16_NT,  };
 private static final byte[] nts_12 = { load32_NT,  };
 private static final byte[] nts_13 = { load16_32_NT,  };
 private static final byte[] nts_14 = { load8_NT,  };
 private static final byte[] nts_15 = { address1reg_NT,  };
 private static final byte[] nts_16 = { address1scaledreg_NT,  };
 private static final byte[] nts_17 = { r_NT, r_NT,  };
 private static final byte[] nts_18 = { r_NT, address1scaledreg_NT,  };
 private static final byte[] nts_19 = { address1scaledreg_NT, r_NT,  };
 private static final byte[] nts_20 = { address1scaledreg_NT, address1reg_NT,  };
 private static final byte[] nts_21 = { address1reg_NT, address1scaledreg_NT,  };
 private static final byte[] nts_22 = { address_NT,  };
 private static final byte[] nts_23 = { load32_NT, riv_NT,  };
 private static final byte[] nts_24 = { riv_NT, load32_NT,  };
 private static final byte[] nts_25 = { riv_NT, riv_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_26 = { r_NT, riv_NT,  };
 private static final byte[] nts_27 = { cz_NT,  };
 private static final byte[] nts_28 = { szp_NT,  };
 private static final byte[] nts_29 = { bittest_NT,  };
 private static final byte[] nts_30 = { boolcmp_NT,  };
 private static final byte[] nts_31 = { r_NT, load32_NT,  };
 private static final byte[] nts_32 = { boolcmp_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_33 = { rlv_NT, rlv_NT,  };
 private static final byte[] nts_34 = { r_NT, riv_NT, any_NT,  };
 private static final byte[] nts_35 = { r_NT, any_NT,  };
 private static final byte[] nts_36 = { load8_NT, any_NT,  };
 private static final byte[] nts_37 = { uload8_NT, riv_NT, any_NT,  };
 private static final byte[] nts_38 = { riv_NT, uload8_NT, any_NT,  };
 private static final byte[] nts_39 = { sload16_NT, any_NT,  };
 private static final byte[] nts_40 = { load32_NT, riv_NT, any_NT,  };
 private static final byte[] nts_41 = { riv_NT, load32_NT, any_NT,  };
 private static final byte[] nts_42 = { boolcmp_NT, any_NT,  };
 private static final byte[] nts_43 = { bittest_NT, any_NT,  };
 private static final byte[] nts_44 = { cz_NT, any_NT,  };
 private static final byte[] nts_45 = { szp_NT, any_NT,  };
 private static final byte[] nts_46 = { r_NT, rlv_NT, any_NT,  };
 private static final byte[] nts_47 = { riv_NT, riv_NT, riv_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_48 = { riv_NT, r_NT,  };
 private static final byte[] nts_49 = { load32_NT, r_NT,  };
 private static final byte[] nts_50 = { riv_NT, riv_NT, r_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_51 = { r_NT, r_NT, r_NT, r_NT,  };
 private static final byte[] nts_52 = { load8_16_32_NT, riv_NT,  };
 private static final byte[] nts_53 = { riv_NT, load8_16_32_NT,  };
 private static final byte[] nts_54 = { r_NT, rlv_NT,  };
 private static final byte[] nts_55 = { rlv_NT, riv_NT,  };
 private static final byte[] nts_56 = { load8_16_32_NT,  };
 private static final byte[] nts_57 = { r_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_58 = { load64_NT,  };
 private static final byte[] nts_59 = { riv_NT, address1scaledreg_NT,  };
 private static final byte[] nts_60 = { address1scaledreg_NT, riv_NT,  };
 private static final byte[] nts_61 = { r_NT, address1scaledreg_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_62 = { address1scaledreg_NT, r_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_63 = { address1scaledreg_NT, address1reg_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_64 = { address1reg_NT, address1scaledreg_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_65 = { address_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_66 = { riv_NT, riv_NT, rlv_NT, rlv_NT,  };
 private static final byte[] nts_67 = { riv_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_68 = { load8_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_69 = { load16_NT, riv_NT, riv_NT,  };
 private static final byte[] nts_70 = { riv_NT, riv_NT, address1scaledreg_NT,  };
 private static final byte[] nts_71 = { riv_NT, address1scaledreg_NT, riv_NT,  };
 private static final byte[] nts_72 = { riv_NT, address1scaledreg_NT, address1reg_NT,  };
 private static final byte[] nts_73 = { riv_NT, address1reg_NT, address1scaledreg_NT,  };
 private static final byte[] nts_74 = { riv_NT, address_NT,  };
 private static final byte[] nts_75 = { uload8_NT, r_NT,  };
 private static final byte[] nts_76 = { r_NT, uload8_NT,  };
 private static final byte[] nts_77 = { any_NT,  };
 private static final byte[] nts_78 = { riv_NT, riv_NT, any_NT,  };
 private static final byte[] nts_79 = { r_NT, float_load_NT,  };
 private static final byte[] nts_80 = { float_load_NT, r_NT,  };
 private static final byte[] nts_81 = { r_NT, double_load_NT,  };
 private static final byte[] nts_82 = { double_load_NT, r_NT,  };
 private static final byte[] nts_83 = { float_load_NT,  };
 private static final byte[] nts_84 = { double_load_NT,  };
 private static final byte[] nts_85 = { rlv_NT,  };
 private static final byte[] nts_86 = { r_NT, r_NT, any_NT,  };
 private static final byte[] nts_87 = { r_NT, float_load_NT, any_NT,  };
 private static final byte[] nts_88 = { r_NT, double_load_NT, any_NT,  };
 private static final byte[] nts_89 = { float_load_NT, r_NT, any_NT,  };
 private static final byte[] nts_90 = { double_load_NT, r_NT, any_NT,  };
 private static final byte[] nts_91 = { r_NT, r_NT, r_NT, float_load_NT,  };
 private static final byte[] nts_92 = { r_NT, r_NT, r_NT, double_load_NT,  };
 private static final byte[] nts_93 = { r_NT, r_NT, float_load_NT, r_NT,  };
 private static final byte[] nts_94 = { r_NT, r_NT, double_load_NT, r_NT,  };
 private static final byte[] nts_95 = { r_NT, r_NT, r_NT,  };
 private static final byte[] nts_96 = { load64_NT, riv_NT, riv_NT,  };

private static final byte[][] nts = {
	null,	/* 0 */
	nts_0,	// 1 
	nts_1,	// 2 
	nts_2,	// 3 
	nts_2,	// 4 
	nts_3,	// 5 
	nts_3,	// 6 
	nts_0,	// 7 
	nts_1,	// 8 
	nts_0,	// 9 
	nts_1,	// 10 
	nts_1,	// 11 
	nts_4,	// 12 
	nts_1,	// 13 
	nts_1,	// 14 
	nts_5,	// 15 
	nts_6,	// 16 
	nts_6,	// 17 
	nts_6,	// 18 
	nts_6,	// 19 
	nts_7,	// 20 
	nts_8,	// 21 
	nts_6,	// 22 
	nts_6,	// 23 
	nts_6,	// 24 
	nts_6,	// 25 
	nts_9,	// 26 
	nts_10,	// 27 
	nts_6,	// 28 
	nts_6,	// 29 
	nts_11,	// 30 
	nts_12,	// 31 
	nts_13,	// 32 
	nts_14,	// 33 
	nts_6,	// 34 
	nts_6,	// 35 
	nts_15,	// 36 
	nts_16,	// 37 
	nts_0,	// 38 
	nts_0,	// 39 
	nts_0,	// 40 
	nts_17,	// 41 
	nts_15,	// 42 
	nts_16,	// 43 
	nts_18,	// 44 
	nts_19,	// 45 
	nts_16,	// 46 
	nts_20,	// 47 
	nts_21,	// 48 
	nts_19,	// 49 
	nts_18,	// 50 
	nts_20,	// 51 
	nts_21,	// 52 
	nts_22,	// 53 
	nts_22,	// 54 
	nts_1,	// 55 
	nts_1,	// 56 
	nts_1,	// 57 
	nts_1,	// 58 
	nts_1,	// 59 
	nts_1,	// 60 
	nts_0,	// 61 
	nts_1,	// 62 
	nts_1,	// 63 
	nts_1,	// 64 
	nts_1,	// 65 
	nts_4,	// 66 
	nts_1,	// 67 
	nts_1,	// 68 
	nts_0,	// 69 
	nts_1,	// 70 
	nts_0,	// 71 
	nts_0,	// 72 
	nts_17,	// 73 
	nts_23,	// 74 
	nts_24,	// 75 
	nts_0,	// 76 
	nts_25,	// 77 
	nts_25,	// 78 
	nts_26,	// 79 
	nts_26,	// 80 
	nts_0,	// 81 
	nts_0,	// 82 
	nts_0,	// 83 
	nts_12,	// 84 
	nts_0,	// 85 
	nts_12,	// 86 
	nts_27,	// 87 
	nts_27,	// 88 
	nts_28,	// 89 
	nts_28,	// 90 
	nts_29,	// 91 
	nts_29,	// 92 
	nts_30,	// 93 
	nts_30,	// 94 
	nts_30,	// 95 
	nts_30,	// 96 
	nts_23,	// 97 
	nts_23,	// 98 
	nts_31,	// 99 
	nts_24,	// 100 
	nts_32,	// 101 
	nts_32,	// 102 
	nts_33,	// 103 
	nts_33,	// 104 
	nts_34,	// 105 
	nts_35,	// 106 
	nts_0,	// 107 
	nts_12,	// 108 
	nts_0,	// 109 
	nts_12,	// 110 
	nts_36,	// 111 
	nts_37,	// 112 
	nts_38,	// 113 
	nts_39,	// 114 
	nts_40,	// 115 
	nts_41,	// 116 
	nts_42,	// 117 
	nts_42,	// 118 
	nts_43,	// 119 
	nts_44,	// 120 
	nts_45,	// 121 
	nts_46,	// 122 
	nts_26,	// 123 
	nts_26,	// 124 
	nts_24,	// 125 
	nts_23,	// 126 
	nts_47,	// 127 
	nts_47,	// 128 
	nts_47,	// 129 
	nts_47,	// 130 
	nts_48,	// 131 
	nts_48,	// 132 
	nts_49,	// 133 
	nts_24,	// 134 
	nts_23,	// 135 
	nts_47,	// 136 
	nts_47,	// 137 
	nts_47,	// 138 
	nts_47,	// 139 
	nts_6,	// 140 
	nts_24,	// 141 
	nts_23,	// 142 
	nts_6,	// 143 
	nts_24,	// 144 
	nts_6,	// 145 
	nts_24,	// 146 
	nts_0,	// 147 
	nts_25,	// 148 
	nts_25,	// 149 
	nts_48,	// 150 
	nts_6,	// 151 
	nts_0,	// 152 
	nts_0,	// 153 
	nts_0,	// 154 
	nts_50,	// 155 
	nts_25,	// 156 
	nts_50,	// 157 
	nts_25,	// 158 
	nts_48,	// 159 
	nts_6,	// 160 
	nts_4,	// 161 
	nts_50,	// 162 
	nts_25,	// 163 
	nts_50,	// 164 
	nts_25,	// 165 
	nts_48,	// 166 
	nts_6,	// 167 
	nts_4,	// 168 
	nts_50,	// 169 
	nts_25,	// 170 
	nts_50,	// 171 
	nts_25,	// 172 
	nts_17,	// 173 
	nts_17,	// 174 
	nts_17,	// 175 
	nts_17,	// 176 
	nts_51,	// 177 
	nts_51,	// 178 
	nts_51,	// 179 
	nts_51,	// 180 
	nts_6,	// 181 
	nts_26,	// 182 
	nts_24,	// 183 
	nts_23,	// 184 
	nts_52,	// 185 
	nts_53,	// 186 
	nts_47,	// 187 
	nts_47,	// 188 
	nts_47,	// 189 
	nts_47,	// 190 
	nts_6,	// 191 
	nts_24,	// 192 
	nts_23,	// 193 
	nts_47,	// 194 
	nts_47,	// 195 
	nts_47,	// 196 
	nts_47,	// 197 
	nts_6,	// 198 
	nts_24,	// 199 
	nts_23,	// 200 
	nts_47,	// 201 
	nts_47,	// 202 
	nts_47,	// 203 
	nts_47,	// 204 
	nts_0,	// 205 
	nts_25,	// 206 
	nts_25,	// 207 
	nts_54,	// 208 
	nts_33,	// 209 
	nts_33,	// 210 
	nts_0,	// 211 
	nts_55,	// 212 
	nts_55,	// 213 
	nts_55,	// 214 
	nts_55,	// 215 
	nts_55,	// 216 
	nts_55,	// 217 
	nts_33,	// 218 
	nts_33,	// 219 
	nts_54,	// 220 
	nts_0,	// 221 
	nts_0,	// 222 
	nts_56,	// 223 
	nts_57,	// 224 
	nts_57,	// 225 
	nts_0,	// 226 
	nts_13,	// 227 
	nts_57,	// 228 
	nts_57,	// 229 
	nts_0,	// 230 
	nts_13,	// 231 
	nts_57,	// 232 
	nts_57,	// 233 
	nts_0,	// 234 
	nts_12,	// 235 
	nts_0,	// 236 
	nts_12,	// 237 
	nts_0,	// 238 
	nts_58,	// 239 
	nts_0,	// 240 
	nts_12,	// 241 
	nts_0,	// 242 
	nts_57,	// 243 
	nts_57,	// 244 
	nts_58,	// 245 
	nts_0,	// 246 
	nts_0,	// 247 
	nts_58,	// 248 
	nts_58,	// 249 
	nts_58,	// 250 
	nts_58,	// 251 
	nts_4,	// 252 
	nts_2,	// 253 
	nts_27,	// 254 
	nts_3,	// 255 
	nts_28,	// 256 
	nts_15,	// 257 
	nts_16,	// 258 
	nts_22,	// 259 
	nts_7,	// 260 
	nts_8,	// 261 
	nts_14,	// 262 
	nts_9,	// 263 
	nts_10,	// 264 
	nts_11,	// 265 
	nts_12,	// 266 
	nts_58,	// 267 
	nts_0,	// 268 
	nts_1,	// 269 
	nts_1,	// 270 
	nts_6,	// 271 
	nts_6,	// 272 
	nts_6,	// 273 
	nts_6,	// 274 
	nts_56,	// 275 
	nts_56,	// 276 
	nts_56,	// 277 
	nts_56,	// 278 
	nts_6,	// 279 
	nts_6,	// 280 
	nts_6,	// 281 
	nts_6,	// 282 
	nts_13,	// 283 
	nts_13,	// 284 
	nts_13,	// 285 
	nts_13,	// 286 
	nts_6,	// 287 
	nts_59,	// 288 
	nts_60,	// 289 
	nts_20,	// 290 
	nts_21,	// 291 
	nts_22,	// 292 
	nts_6,	// 293 
	nts_6,	// 294 
	nts_6,	// 295 
	nts_6,	// 296 
	nts_18,	// 297 
	nts_19,	// 298 
	nts_20,	// 299 
	nts_21,	// 300 
	nts_22,	// 301 
	nts_22,	// 302 
	nts_6,	// 303 
	nts_25,	// 304 
	nts_61,	// 305 
	nts_62,	// 306 
	nts_63,	// 307 
	nts_64,	// 308 
	nts_65,	// 309 
	nts_65,	// 310 
	nts_25,	// 311 
	nts_61,	// 312 
	nts_62,	// 313 
	nts_63,	// 314 
	nts_64,	// 315 
	nts_65,	// 316 
	nts_65,	// 317 
	nts_25,	// 318 
	nts_61,	// 319 
	nts_62,	// 320 
	nts_63,	// 321 
	nts_64,	// 322 
	nts_65,	// 323 
	nts_65,	// 324 
	nts_66,	// 325 
	nts_67,	// 326 
	nts_68,	// 327 
	nts_67,	// 328 
	nts_68,	// 329 
	nts_67,	// 330 
	nts_69,	// 331 
	nts_67,	// 332 
	nts_69,	// 333 
	nts_67,	// 334 
	nts_70,	// 335 
	nts_71,	// 336 
	nts_72,	// 337 
	nts_73,	// 338 
	nts_74,	// 339 
	nts_67,	// 340 
	nts_57,	// 341 
	nts_6,	// 342 
	nts_57,	// 343 
	nts_6,	// 344 
	nts_26,	// 345 
	nts_0,	// 346 
	nts_14,	// 347 
	nts_75,	// 348 
	nts_76,	// 349 
	nts_9,	// 350 
	nts_23,	// 351 
	nts_31,	// 352 
	nts_30,	// 353 
	nts_30,	// 354 
	nts_27,	// 355 
	nts_28,	// 356 
	nts_29,	// 357 
	nts_26,	// 358 
	nts_23,	// 359 
	nts_24,	// 360 
	nts_33,	// 361 
	nts_33,	// 362 
	nts_1,	// 363 
	nts_0,	// 364 
	nts_1,	// 365 
	nts_1,	// 366 
	nts_1,	// 367 
	nts_1,	// 368 
	nts_1,	// 369 
	nts_0,	// 370 
	nts_1,	// 371 
	nts_35,	// 372 
	nts_77,	// 373 
	nts_78,	// 374 
	nts_77,	// 375 
	nts_35,	// 376 
	nts_78,	// 377 
	nts_77,	// 378 
	nts_1,	// 379 
	nts_5,	// 380 
	nts_17,	// 381 
	nts_49,	// 382 
	nts_0,	// 383 
	nts_17,	// 384 
	nts_49,	// 385 
	nts_0,	// 386 
	nts_48,	// 387 
	nts_31,	// 388 
	nts_17,	// 389 
	nts_49,	// 390 
	nts_17,	// 391 
	nts_79,	// 392 
	nts_80,	// 393 
	nts_17,	// 394 
	nts_81,	// 395 
	nts_82,	// 396 
	nts_17,	// 397 
	nts_79,	// 398 
	nts_17,	// 399 
	nts_81,	// 400 
	nts_17,	// 401 
	nts_79,	// 402 
	nts_80,	// 403 
	nts_17,	// 404 
	nts_81,	// 405 
	nts_82,	// 406 
	nts_17,	// 407 
	nts_79,	// 408 
	nts_17,	// 409 
	nts_81,	// 410 
	nts_0,	// 411 
	nts_0,	// 412 
	nts_0,	// 413 
	nts_0,	// 414 
	nts_17,	// 415 
	nts_17,	// 416 
	nts_0,	// 417 
	nts_0,	// 418 
	nts_0,	// 419 
	nts_0,	// 420 
	nts_6,	// 421 
	nts_6,	// 422 
	nts_6,	// 423 
	nts_6,	// 424 
	nts_6,	// 425 
	nts_6,	// 426 
	nts_6,	// 427 
	nts_6,	// 428 
	nts_57,	// 429 
	nts_57,	// 430 
	nts_57,	// 431 
	nts_57,	// 432 
	nts_4,	// 433 
	nts_12,	// 434 
	nts_4,	// 435 
	nts_12,	// 436 
	nts_0,	// 437 
	nts_83,	// 438 
	nts_0,	// 439 
	nts_84,	// 440 
	nts_0,	// 441 
	nts_0,	// 442 
	nts_0,	// 443 
	nts_0,	// 444 
	nts_0,	// 445 
	nts_83,	// 446 
	nts_0,	// 447 
	nts_84,	// 448 
	nts_4,	// 449 
	nts_12,	// 450 
	nts_85,	// 451 
	nts_58,	// 452 
	nts_1,	// 453 
	nts_1,	// 454 
	nts_1,	// 455 
	nts_1,	// 456 
	nts_17,	// 457 
	nts_79,	// 458 
	nts_80,	// 459 
	nts_17,	// 460 
	nts_81,	// 461 
	nts_82,	// 462 
	nts_86,	// 463 
	nts_87,	// 464 
	nts_88,	// 465 
	nts_89,	// 466 
	nts_90,	// 467 
	nts_86,	// 468 
	nts_91,	// 469 
	nts_92,	// 470 
	nts_93,	// 471 
	nts_94,	// 472 
	nts_87,	// 473 
	nts_88,	// 474 
	nts_95,	// 475 
	nts_95,	// 476 
	nts_95,	// 477 
	nts_95,	// 478 
	nts_95,	// 479 
	nts_95,	// 480 
	nts_95,	// 481 
	nts_95,	// 482 
	nts_96,	// 483 
	nts_96,	// 484 
};

/*static final byte arity[] = {
	0,	// 0=GET_CAUGHT_EXCEPTION
	1,	// 1=SET_CAUGHT_EXCEPTION
	-1,	// 2=NEW
	-1,	// 3=NEW_UNRESOLVED
	-1,	// 4=NEWARRAY
	-1,	// 5=NEWARRAY_UNRESOLVED
	-1,	// 6=ATHROW
	-1,	// 7=CHECKCAST
	-1,	// 8=CHECKCAST_NOTNULL
	-1,	// 9=CHECKCAST_UNRESOLVED
	-1,	// 10=MUST_IMPLEMENT_INTERFACE
	-1,	// 11=INSTANCEOF
	-1,	// 12=INSTANCEOF_NOTNULL
	-1,	// 13=INSTANCEOF_UNRESOLVED
	-1,	// 14=MONITORENTER
	-1,	// 15=MONITOREXIT
	-1,	// 16=NEWOBJMULTIARRAY
	-1,	// 17=GETSTATIC
	-1,	// 18=PUTSTATIC
	-1,	// 19=GETFIELD
	-1,	// 20=PUTFIELD
	-1,	// 21=INT_ZERO_CHECK
	-1,	// 22=LONG_ZERO_CHECK
	-1,	// 23=BOUNDS_CHECK
	-1,	// 24=OBJARRAY_STORE_CHECK
	-1,	// 25=OBJARRAY_STORE_CHECK_NOTNULL
	0,	// 26=IG_PATCH_POINT
	-1,	// 27=IG_CLASS_TEST
	-1,	// 28=IG_METHOD_TEST
	-1,	// 29=TABLESWITCH
	-1,	// 30=LOOKUPSWITCH
	2,	// 31=INT_ALOAD
	2,	// 32=LONG_ALOAD
	2,	// 33=FLOAT_ALOAD
	2,	// 34=DOUBLE_ALOAD
	-1,	// 35=REF_ALOAD
	2,	// 36=UBYTE_ALOAD
	2,	// 37=BYTE_ALOAD
	2,	// 38=USHORT_ALOAD
	2,	// 39=SHORT_ALOAD
	2,	// 40=INT_ASTORE
	2,	// 41=LONG_ASTORE
	2,	// 42=FLOAT_ASTORE
	2,	// 43=DOUBLE_ASTORE
	-1,	// 44=REF_ASTORE
	2,	// 45=BYTE_ASTORE
	2,	// 46=SHORT_ASTORE
	2,	// 47=INT_IFCMP
	2,	// 48=INT_IFCMP2
	2,	// 49=LONG_IFCMP
	2,	// 50=FLOAT_IFCMP
	2,	// 51=DOUBLE_IFCMP
	-1,	// 52=REF_IFCMP
	-1,	// 53=LABEL
	-1,	// 54=BBEND
	0,	// 55=UNINT_BEGIN
	0,	// 56=UNINT_END
	0,	// 57=READ_CEILING
	0,	// 58=WRITE_FLOOR
	-1,	// 59=PHI
	-1,	// 60=SPLIT
	-1,	// 61=PI
	0,	// 62=NOP
	1,	// 63=INT_MOVE
	1,	// 64=LONG_MOVE
	1,	// 65=FLOAT_MOVE
	1,	// 66=DOUBLE_MOVE
	-1,	// 67=REF_MOVE
	0,	// 68=GUARD_MOVE
	-1,	// 69=INT_COND_MOVE
	-1,	// 70=LONG_COND_MOVE
	-1,	// 71=FLOAT_COND_MOVE
	-1,	// 72=DOUBLE_COND_MOVE
	-1,	// 73=REF_COND_MOVE
	-1,	// 74=GUARD_COND_MOVE
	0,	// 75=GUARD_COMBINE
	-1,	// 76=REF_ADD
	2,	// 77=INT_ADD
	2,	// 78=LONG_ADD
	2,	// 79=FLOAT_ADD
	2,	// 80=DOUBLE_ADD
	-1,	// 81=REF_SUB
	2,	// 82=INT_SUB
	2,	// 83=LONG_SUB
	2,	// 84=FLOAT_SUB
	2,	// 85=DOUBLE_SUB
	2,	// 86=INT_MUL
	2,	// 87=LONG_MUL
	2,	// 88=FLOAT_MUL
	2,	// 89=DOUBLE_MUL
	2,	// 90=INT_DIV
	-1,	// 91=LONG_DIV
	2,	// 92=FLOAT_DIV
	2,	// 93=DOUBLE_DIV
	2,	// 94=INT_REM
	-1,	// 95=LONG_REM
	2,	// 96=FLOAT_REM
	2,	// 97=DOUBLE_REM
	-1,	// 98=REF_NEG
	1,	// 99=INT_NEG
	1,	// 100=LONG_NEG
	1,	// 101=FLOAT_NEG
	1,	// 102=DOUBLE_NEG
	1,	// 103=FLOAT_SQRT
	1,	// 104=DOUBLE_SQRT
	-1,	// 105=REF_SHL
	2,	// 106=INT_SHL
	2,	// 107=LONG_SHL
	-1,	// 108=REF_SHR
	2,	// 109=INT_SHR
	2,	// 110=LONG_SHR
	-1,	// 111=REF_USHR
	2,	// 112=INT_USHR
	2,	// 113=LONG_USHR
	-1,	// 114=REF_AND
	2,	// 115=INT_AND
	2,	// 116=LONG_AND
	-1,	// 117=REF_OR
	2,	// 118=INT_OR
	2,	// 119=LONG_OR
	-1,	// 120=REF_XOR
	2,	// 121=INT_XOR
	-1,	// 122=REF_NOT
	1,	// 123=INT_NOT
	1,	// 124=LONG_NOT
	2,	// 125=LONG_XOR
	-1,	// 126=INT_2ADDRSigExt
	-1,	// 127=INT_2ADDRZerExt
	-1,	// 128=LONG_2ADDR
	-1,	// 129=ADDR_2INT
	1,	// 130=ADDR_2LONG
	1,	// 131=INT_2LONG
	1,	// 132=INT_2FLOAT
	1,	// 133=INT_2DOUBLE
	1,	// 134=LONG_2INT
	1,	// 135=LONG_2FLOAT
	1,	// 136=LONG_2DOUBLE
	1,	// 137=FLOAT_2INT
	1,	// 138=FLOAT_2LONG
	1,	// 139=FLOAT_2DOUBLE
	1,	// 140=DOUBLE_2INT
	1,	// 141=DOUBLE_2LONG
	1,	// 142=DOUBLE_2FLOAT
	1,	// 143=INT_2BYTE
	1,	// 144=INT_2USHORT
	1,	// 145=INT_2SHORT
	2,	// 146=LONG_CMP
	-1,	// 147=FLOAT_CMPL
	-1,	// 148=FLOAT_CMPG
	-1,	// 149=DOUBLE_CMPL
	-1,	// 150=DOUBLE_CMPG
	1,	// 151=RETURN
	1,	// 152=NULL_CHECK
	0,	// 153=GOTO
	1,	// 154=BOOLEAN_NOT
	2,	// 155=BOOLEAN_CMP_INT
	-1,	// 156=BOOLEAN_CMP_ADDR
	2,	// 157=BOOLEAN_CMP_LONG
	-1,	// 158=BOOLEAN_CMP_FLOAT
	-1,	// 159=BOOLEAN_CMP_DOUBLE
	2,	// 160=BYTE_LOAD
	2,	// 161=UBYTE_LOAD
	2,	// 162=SHORT_LOAD
	2,	// 163=USHORT_LOAD
	-1,	// 164=REF_LOAD
	-1,	// 165=REF_STORE
	2,	// 166=INT_LOAD
	2,	// 167=LONG_LOAD
	2,	// 168=FLOAT_LOAD
	2,	// 169=DOUBLE_LOAD
	2,	// 170=BYTE_STORE
	2,	// 171=SHORT_STORE
	2,	// 172=INT_STORE
	2,	// 173=LONG_STORE
	2,	// 174=FLOAT_STORE
	2,	// 175=DOUBLE_STORE
	2,	// 176=PREPARE_INT
	-1,	// 177=PREPARE_ADDR
	2,	// 178=PREPARE_LONG
	2,	// 179=ATTEMPT_INT
	-1,	// 180=ATTEMPT_ADDR
	2,	// 181=ATTEMPT_LONG
	2,	// 182=CALL
	2,	// 183=SYSCALL
	0,	// 184=YIELDPOINT_PROLOGUE
	0,	// 185=YIELDPOINT_EPILOGUE
	0,	// 186=YIELDPOINT_BACKEDGE
	2,	// 187=YIELDPOINT_OSR
	-1,	// 188=OSR_BARRIER
	0,	// 189=IR_PROLOGUE
	0,	// 190=RESOLVE
	-1,	// 191=RESOLVE_MEMBER
	0,	// 192=GET_TIME_BASE
	-1,	// 193=INSTRUMENTED_EVENT_COUNTER
	2,	// 194=TRAP_IF
	0,	// 195=TRAP
	1,	// 196=FLOAT_AS_INT_BITS
	1,	// 197=INT_BITS_AS_FLOAT
	1,	// 198=DOUBLE_AS_LONG_BITS
	1,	// 199=LONG_BITS_AS_DOUBLE
	-1,	// 200=ARRAYLENGTH
	-1,	// 201=GET_OBJ_TIB
	-1,	// 202=GET_CLASS_TIB
	-1,	// 203=GET_TYPE_FROM_TIB
	-1,	// 204=GET_SUPERCLASS_IDS_FROM_TIB
	-1,	// 205=GET_DOES_IMPLEMENT_FROM_TIB
	-1,	// 206=GET_ARRAY_ELEMENT_TIB_FROM_TIB
	1,	// 207=LOWTABLESWITCH
	0,	// 208=ADDRESS_CONSTANT
	0,	// 209=INT_CONSTANT
	0,	// 210=LONG_CONSTANT
	0,	// 211=REGISTER
	2,	// 212=OTHER_OPERAND
	0,	// 213=NULL
	0,	// 214=BRANCH_TARGET
	1,	// 215=MATERIALIZE_FP_CONSTANT
	0,	// 216=GET_CURRENT_PROCESSOR
	-1,	// 217=ROUND_TO_ZERO
	0,	// 218=CLEAR_FLOATING_POINT_STATE
	1,	// 219=PREFETCH
	0,	// 220=PAUSE
	-1,	// 221=FP_ADD
	-1,	// 222=FP_SUB
	-1,	// 223=FP_MUL
	-1,	// 224=FP_DIV
	-1,	// 225=FP_NEG
	-1,	// 226=FP_REM
	-1,	// 227=INT_2FP
	-1,	// 228=LONG_2FP
	2,	// 229=CMP_CMOV
	2,	// 230=FCMP_CMOV
	2,	// 231=LCMP_CMOV
	-1,	// 232=CMP_FCMOV
	2,	// 233=FCMP_FCMOV
	-1,	// 234=CALL_SAVE_VOLATILE
	-1,	// 235=MIR_START
	-1,	// 236=REQUIRE_ESP
	-1,	// 237=ADVISE_ESP
	-1,	// 238=MIR_LOWTABLESWITCH
	-1,	// 239=IA32_METHODSTART
	-1,	// 240=IA32_FCLEAR
	-1,	// 241=DUMMY_DEF
	-1,	// 242=DUMMY_USE
	-1,	// 243=IA32_FMOV_ENDING_LIVE_RANGE
	-1,	// 244=IA32_FMOV
	-1,	// 245=IA32_TRAPIF
	-1,	// 246=IA32_OFFSET
	-1,	// 247=IA32_LOCK_CMPXCHG
	-1,	// 248=IA32_LOCK_CMPXCHG8B
	-1,	// 249=IA32_ADC
	-1,	// 250=IA32_ADD
	-1,	// 251=IA32_AND
	-1,	// 252=IA32_BSWAP
	-1,	// 253=IA32_BT
	-1,	// 254=IA32_BTC
	-1,	// 255=IA32_BTR
	-1,	// 256=IA32_BTS
	-1,	// 257=IA32_SYSCALL
	-1,	// 258=IA32_CALL
	-1,	// 259=IA32_CDQ
	-1,	// 260=IA32_CDO
	-1,	// 261=IA32_CDQE
	-1,	// 262=IA32_CMOV
	-1,	// 263=IA32_CMP
	-1,	// 264=IA32_CMPXCHG
	-1,	// 265=IA32_CMPXCHG8B
	-1,	// 266=IA32_DEC
	-1,	// 267=IA32_DIV
	-1,	// 268=IA32_FADD
	-1,	// 269=IA32_FADDP
	-1,	// 270=IA32_FCHS
	-1,	// 271=IA32_FCMOV
	-1,	// 272=IA32_FCOMI
	-1,	// 273=IA32_FCOMIP
	-1,	// 274=IA32_FDIV
	-1,	// 275=IA32_FDIVP
	-1,	// 276=IA32_FDIVR
	-1,	// 277=IA32_FDIVRP
	-1,	// 278=IA32_FEXAM
	-1,	// 279=IA32_FXCH
	-1,	// 280=IA32_FFREE
	-1,	// 281=IA32_FIADD
	-1,	// 282=IA32_FIDIV
	-1,	// 283=IA32_FIDIVR
	-1,	// 284=IA32_FILD
	-1,	// 285=IA32_FIMUL
	-1,	// 286=IA32_FINIT
	-1,	// 287=IA32_FIST
	-1,	// 288=IA32_FISTP
	-1,	// 289=IA32_FISUB
	-1,	// 290=IA32_FISUBR
	-1,	// 291=IA32_FLD
	-1,	// 292=IA32_FLDCW
	-1,	// 293=IA32_FLD1
	-1,	// 294=IA32_FLDL2T
	-1,	// 295=IA32_FLDL2E
	-1,	// 296=IA32_FLDPI
	-1,	// 297=IA32_FLDLG2
	-1,	// 298=IA32_FLDLN2
	-1,	// 299=IA32_FLDZ
	-1,	// 300=IA32_FMUL
	-1,	// 301=IA32_FMULP
	-1,	// 302=IA32_FNSTCW
	-1,	// 303=IA32_FNINIT
	-1,	// 304=IA32_FNSAVE
	-1,	// 305=IA32_FPREM
	-1,	// 306=IA32_FRSTOR
	-1,	// 307=IA32_FST
	-1,	// 308=IA32_FSTCW
	-1,	// 309=IA32_FSTP
	-1,	// 310=IA32_FSUB
	-1,	// 311=IA32_FSUBP
	-1,	// 312=IA32_FSUBR
	-1,	// 313=IA32_FSUBRP
	-1,	// 314=IA32_FUCOMI
	-1,	// 315=IA32_FUCOMIP
	-1,	// 316=IA32_IDIV
	-1,	// 317=IA32_IMUL1
	-1,	// 318=IA32_IMUL2
	-1,	// 319=IA32_INC
	-1,	// 320=IA32_INT
	-1,	// 321=IA32_JCC
	-1,	// 322=IA32_JCC2
	-1,	// 323=IA32_JMP
	-1,	// 324=IA32_LEA
	-1,	// 325=IA32_LOCK
	-1,	// 326=IA32_MOV
	-1,	// 327=IA32_MOVZX__B
	-1,	// 328=IA32_MOVSX__B
	-1,	// 329=IA32_MOVZX__W
	-1,	// 330=IA32_MOVSX__W
	-1,	// 331=IA32_MOVZXQ__B
	-1,	// 332=IA32_MOVSXQ__B
	-1,	// 333=IA32_MOVZXQ__W
	-1,	// 334=IA32_MOVSXQ__W
	-1,	// 335=IA32_MUL
	-1,	// 336=IA32_NEG
	-1,	// 337=IA32_NOT
	-1,	// 338=IA32_OR
	-1,	// 339=IA32_PAUSE
	-1,	// 340=IA32_PREFETCHNTA
	-1,	// 341=IA32_POP
	-1,	// 342=IA32_PUSH
	-1,	// 343=IA32_RCL
	-1,	// 344=IA32_RCR
	-1,	// 345=IA32_ROL
	-1,	// 346=IA32_ROR
	-1,	// 347=IA32_RET
	-1,	// 348=IA32_SAL
	-1,	// 349=IA32_SAR
	-1,	// 350=IA32_SHL
	-1,	// 351=IA32_SHR
	-1,	// 352=IA32_SBB
	-1,	// 353=IA32_SET__B
	-1,	// 354=IA32_SHLD
	-1,	// 355=IA32_SHRD
	-1,	// 356=IA32_SUB
	-1,	// 357=IA32_TEST
	-1,	// 358=IA32_XOR
	-1,	// 359=IA32_RDTSC
	-1,	// 360=IA32_ADDSS
	-1,	// 361=IA32_SUBSS
	-1,	// 362=IA32_MULSS
	-1,	// 363=IA32_DIVSS
	-1,	// 364=IA32_ADDSD
	-1,	// 365=IA32_SUBSD
	-1,	// 366=IA32_MULSD
	-1,	// 367=IA32_DIVSD
	-1,	// 368=IA32_ANDPS
	-1,	// 369=IA32_ANDPD
	-1,	// 370=IA32_ANDNPS
	-1,	// 371=IA32_ANDNPD
	-1,	// 372=IA32_ORPS
	-1,	// 373=IA32_ORPD
	-1,	// 374=IA32_XORPS
	-1,	// 375=IA32_XORPD
	-1,	// 376=IA32_UCOMISS
	-1,	// 377=IA32_UCOMISD
	-1,	// 378=IA32_CMPEQSS
	-1,	// 379=IA32_CMPLTSS
	-1,	// 380=IA32_CMPLESS
	-1,	// 381=IA32_CMPUNORDSS
	-1,	// 382=IA32_CMPNESS
	-1,	// 383=IA32_CMPNLTSS
	-1,	// 384=IA32_CMPNLESS
	-1,	// 385=IA32_CMPORDSS
	-1,	// 386=IA32_CMPEQSD
	-1,	// 387=IA32_CMPLTSD
	-1,	// 388=IA32_CMPLESD
	-1,	// 389=IA32_CMPUNORDSD
	-1,	// 390=IA32_CMPNESD
	-1,	// 391=IA32_CMPNLTSD
	-1,	// 392=IA32_CMPNLESD
	-1,	// 393=IA32_CMPORDSD
	-1,	// 394=IA32_MOVLPD
	-1,	// 395=IA32_MOVLPS
	-1,	// 396=IA32_MOVSS
	-1,	// 397=IA32_MOVSD
	-1,	// 398=IA32_MOVD
	-1,	// 399=IA32_MOVQ
	-1,	// 400=IA32_PSLLQ
	-1,	// 401=IA32_PSRLQ
	-1,	// 402=IA32_SQRTSS
	-1,	// 403=IA32_SQRTSD
	-1,	// 404=IA32_CVTSI2SS
	-1,	// 405=IA32_CVTSS2SD
	-1,	// 406=IA32_CVTSS2SI
	-1,	// 407=IA32_CVTTSS2SI
	-1,	// 408=IA32_CVTSI2SD
	-1,	// 409=IA32_CVTSD2SS
	-1,	// 410=IA32_CVTSD2SI
	-1,	// 411=IA32_CVTTSD2SI
	-1,	// 412=IA32_CVTSI2SDQ
	-1,	// 413=IA32_CVTSD2SIQ
	-1,	// 414=IA32_CVTTSD2SIQ
	-1,	// 415=MIR_END
};*/

static final char[][] decode = {null,
	{// stm_NT
	0,
	1,
	55,
	56,
	57,
	58,
	59,
	60,
	61,
	62,
	63,
	66,
	67,
	69,
	70,
	71,
	72,
	73,
	74,
	75,
	77,
	78,
	101,
	102,
	127,
	128,
	129,
	130,
	136,
	137,
	138,
	139,
	148,
	149,
	155,
	156,
	157,
	158,
	162,
	163,
	164,
	165,
	169,
	170,
	171,
	172,
	187,
	188,
	189,
	190,
	194,
	195,
	196,
	197,
	201,
	202,
	203,
	204,
	206,
	207,
	224,
	225,
	228,
	229,
	232,
	233,
	243,
	244,
	311,
	312,
	313,
	314,
	315,
	316,
	317,
	318,
	319,
	320,
	321,
	322,
	323,
	324,
	326,
	327,
	328,
	329,
	330,
	331,
	332,
	333,
	334,
	335,
	336,
	337,
	338,
	339,
	340,
	341,
	342,
	343,
	344,
	345,
	346,
	347,
	348,
	349,
	350,
	351,
	352,
	353,
	354,
	355,
	356,
	357,
	358,
	359,
	360,
	361,
	363,
	364,
	365,
	366,
	367,
	368,
	369,
	370,
	371,
	380,
	429,
	430,
	431,
	432,
	456,
	457,
	458,
	459,
	460,
	461,
	462,
	483,
	484,
},
	{// r_NT
	0,
	2,
	3,
	5,
	49,
	50,
	51,
	52,
	53,
	54,
	64,
	65,
	68,
	76,
	79,
	81,
	83,
	84,
	85,
	86,
	87,
	89,
	91,
	93,
	95,
	97,
	99,
	103,
	105,
	106,
	107,
	108,
	109,
	110,
	111,
	112,
	113,
	114,
	115,
	116,
	117,
	118,
	119,
	120,
	121,
	122,
	124,
	132,
	133,
	140,
	141,
	142,
	143,
	144,
	145,
	146,
	153,
	173,
	174,
	175,
	176,
	177,
	178,
	179,
	180,
	205,
	208,
	209,
	210,
	211,
	212,
	213,
	214,
	215,
	216,
	217,
	218,
	219,
	220,
	221,
	222,
	223,
	227,
	230,
	231,
	234,
	235,
	236,
	237,
	238,
	239,
	240,
	241,
	242,
	245,
	246,
	247,
	248,
	249,
	252,
	268,
	269,
	270,
	271,
	272,
	273,
	274,
	276,
	277,
	278,
	279,
	280,
	281,
	282,
	283,
	284,
	285,
	286,
	287,
	288,
	289,
	290,
	291,
	292,
	293,
	294,
	295,
	296,
	297,
	298,
	299,
	300,
	301,
	302,
	303,
	304,
	305,
	306,
	307,
	308,
	309,
	310,
	325,
	362,
	372,
	373,
	374,
	375,
	376,
	377,
	378,
	379,
	391,
	392,
	393,
	394,
	395,
	396,
	397,
	398,
	399,
	400,
	401,
	402,
	403,
	404,
	405,
	406,
	407,
	408,
	409,
	410,
	411,
	412,
	413,
	414,
	415,
	416,
	417,
	418,
	419,
	420,
	421,
	423,
	425,
	427,
	433,
	434,
	435,
	436,
	437,
	438,
	439,
	440,
	441,
	442,
	443,
	444,
	445,
	447,
	449,
	451,
	453,
	463,
	464,
	465,
	466,
	467,
	468,
	469,
	470,
	471,
	472,
	473,
	474,
	475,
	476,
	477,
	478,
	479,
	480,
	481,
	482,
},
	{// czr_NT
	0,
	123,
	125,
	126,
	131,
	134,
	135,
	253,
},
	{// cz_NT
	0,
	4,
	254,
},
	{// szpr_NT
	0,
	147,
	150,
	151,
	152,
	154,
	159,
	160,
	161,
	166,
	167,
	168,
	181,
	183,
	184,
	191,
	192,
	193,
	198,
	199,
	200,
	226,
	255,
},
	{// szp_NT
	0,
	6,
	182,
	185,
	186,
	256,
},
	{// riv_NT
	0,
	7,
	8,
},
	{// rlv_NT
	0,
	9,
	10,
},
	{// any_NT
	0,
	11,
	12,
	13,
	14,
	15,
},
	{// sload8_NT
	0,
	16,
	17,
	260,
},
	{// uload8_NT
	0,
	18,
	19,
	261,
	275,
},
	{// load8_NT
	0,
	20,
	21,
	262,
},
	{// sload16_NT
	0,
	22,
	23,
	263,
},
	{// uload16_NT
	0,
	24,
	25,
	264,
},
	{// load16_NT
	0,
	26,
	27,
	265,
},
	{// load32_NT
	0,
	28,
	29,
	250,
	251,
	266,
	446,
},
	{// load16_32_NT
	0,
	30,
	31,
},
	{// load8_16_32_NT
	0,
	32,
	33,
},
	{// load64_NT
	0,
	34,
	35,
	267,
	448,
},
	{// address1scaledreg_NT
	0,
	36,
	38,
	43,
	258,
},
	{// address1reg_NT
	0,
	39,
	40,
	42,
	257,
},
	{// address_NT
	0,
	37,
	41,
	44,
	45,
	46,
	47,
	48,
	259,
},
	{// boolcmp_NT
	0,
	80,
	82,
	88,
	90,
	92,
	94,
	96,
	98,
	100,
	104,
},
	{// bittest_NT
	0,
	381,
	382,
	383,
	384,
	385,
	386,
	387,
	388,
	389,
	390,
},
	{// float_load_NT
	0,
	426,
	428,
	450,
	454,
},
	{// double_load_NT
	0,
	422,
	424,
	452,
	455,
},
};

static void closure_r(BURS_TreeNode p, int c) {
	if (c < p.cost_rlv) {
		p.cost_rlv = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFFFC) | 0x1; // p.rlv = 1
	}
	if (c < p.cost_riv) {
		p.cost_riv = (char)(c);
		p.word0 = (p.word0 & 0x9FFFFFFF) | 0x20000000; // p.riv = 1
		closure_riv(p, c);
	}
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x1; // p.stm = 1
	}
}

static void closure_czr(BURS_TreeNode p, int c) {
	if (c < p.cost_cz) {
		p.cost_cz = (char)(c);
		p.word0 = (p.word0 & 0xFFE7FFFF) | 0x80000; // p.cz = 1
	}
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x200; // p.r = 2
		closure_r(p, c);
	}
}

static void closure_szpr(BURS_TreeNode p, int c) {
	if (c < p.cost_szp) {
		p.cost_szp = (char)(c);
		p.word0 = (p.word0 & 0xE3FFFFFF) | 0x4000000; // p.szp = 1
	}
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x300; // p.r = 3
		closure_r(p, c);
	}
}

static void closure_riv(BURS_TreeNode p, int c) {
	if (c < p.cost_any) {
		p.cost_any = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFFE3) | 0x8; // p.any = 2
	}
}

static void closure_sload8(BURS_TreeNode p, int c) {
	if (c < p.cost_load8) {
		p.cost_load8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFF3FF) | 0x400; // p.load8 = 1
		closure_load8(p, c);
	}
}

static void closure_uload8(BURS_TreeNode p, int c) {
	if (c < p.cost_load8) {
		p.cost_load8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFF3FF) | 0x800; // p.load8 = 2
		closure_load8(p, c);
	}
}

static void closure_load8(BURS_TreeNode p, int c) {
	if (c < p.cost_load8_16_32) {
		p.cost_load8_16_32 = (char)(c);
		p.word1 = (p.word1 & 0xFE7FFFFF) | 0x1000000; // p.load8_16_32 = 2
	}
}

static void closure_sload16(BURS_TreeNode p, int c) {
	if (c < p.cost_load16) {
		p.cost_load16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFCFFFF) | 0x10000; // p.load16 = 1
		closure_load16(p, c);
	}
}

static void closure_uload16(BURS_TreeNode p, int c) {
	if (c < p.cost_load16) {
		p.cost_load16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFCFFFF) | 0x20000; // p.load16 = 2
		closure_load16(p, c);
	}
}

static void closure_load16(BURS_TreeNode p, int c) {
	if (c < p.cost_load16_32) {
		p.cost_load16_32 = (char)(c);
		p.word1 = (p.word1 & 0xFF9FFFFF) | 0x200000; // p.load16_32 = 1
		closure_load16_32(p, c);
	}
}

static void closure_load32(BURS_TreeNode p, int c) {
	if (c < p.cost_load16_32) {
		p.cost_load16_32 = (char)(c);
		p.word1 = (p.word1 & 0xFF9FFFFF) | 0x400000; // p.load16_32 = 2
		closure_load16_32(p, c);
	}
}

static void closure_load16_32(BURS_TreeNode p, int c) {
	if (c < p.cost_load8_16_32) {
		p.cost_load8_16_32 = (char)(c);
		p.word1 = (p.word1 & 0xFE7FFFFF) | 0x800000; // p.load8_16_32 = 1
	}
}

static void closure_address1scaledreg(BURS_TreeNode p, int c) {
	if (c < p.cost_address) {
		p.cost_address = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFF87) | 0x8; // p.address = 1
	}
}

static void closure_address1reg(BURS_TreeNode p, int c) {
	if (c < p.cost_address1scaledreg) {
		p.cost_address1scaledreg = (char)(c);
		p.word1 = (p.word1 & 0x8FFFFFFF) | 0x10000000; // p.address1scaledreg = 1
		closure_address1scaledreg(p, c);
	}
}

private void label_GET_CAUGHT_EXCEPTION(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// r: GET_CAUGHT_EXCEPTION
	if (11 < p.cost_r) {
		p.cost_r = (char)(11);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC00; // p.r = 12
		closure_r(p, 11);
	}
}

private void label_SET_CAUGHT_EXCEPTION(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// stm: SET_CAUGHT_EXCEPTION(r)
	c = STATE(lchild).cost_r + 11;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0xD; // p.stm = 13
	}
}

private void label_IG_PATCH_POINT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: IG_PATCH_POINT
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x2; // p.stm = 2
	}
}

private void label_INT_ALOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// load32: INT_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_load32) {
		p.cost_load32 = (char)(c);
		p.word1 = (p.word1 & 0xFFE3FFFF) | 0x80000; // p.load32 = 2
		closure_load32(p, c);
	}
	// r: INT_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7C00; // p.r = 124
		closure_r(p, c);
	}
}

private void label_LONG_ALOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// load64: LONG_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_load64) {
		p.cost_load64 = (char)(c);
		p.word1 = (p.word1 & 0xF1FFFFFF) | 0x4000000; // p.load64 = 2
	}
	// r: LONG_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 30;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7E00; // p.r = 126
		closure_r(p, c);
	}
}

private void label_FLOAT_ALOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: FLOAT_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB900; // p.r = 185
		closure_r(p, c);
	}
	// float_load: FLOAT_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_float_load) {
		p.cost_float_load = (char)(c);
		p.word2 = (p.word2 & 0xFFFC7FFF) | 0x10000; // p.float_load = 2
	}
}

private void label_DOUBLE_ALOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: DOUBLE_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB700; // p.r = 183
		closure_r(p, c);
	}
	// double_load: DOUBLE_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_double_load) {
		p.cost_double_load = (char)(c);
		p.word2 = (p.word2 & 0xFFE3FFFF) | 0x80000; // p.double_load = 2
	}
}

private void label_UBYTE_ALOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// uload8: UBYTE_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_uload8) {
		p.cost_uload8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFC7F) | 0x100; // p.uload8 = 2
		closure_uload8(p, c);
	}
	// r: UBYTE_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6A00; // p.r = 106
		closure_r(p, c);
	}
}

private void label_BYTE_ALOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// sload8: BYTE_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_sload8) {
		p.cost_sload8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFF9F) | 0x40; // p.sload8 = 2
		closure_sload8(p, c);
	}
	// r: BYTE_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6800; // p.r = 104
		closure_r(p, c);
	}
}

private void label_USHORT_ALOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// uload16: USHORT_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_uload16) {
		p.cost_uload16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFF3FFF) | 0x8000; // p.uload16 = 2
		closure_uload16(p, c);
	}
	// r: USHORT_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7100; // p.r = 113
		closure_r(p, c);
	}
}

private void label_SHORT_ALOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// sload16: SHORT_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_sload16) {
		p.cost_sload16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFCFFF) | 0x2000; // p.sload16 = 2
		closure_sload16(p, c);
	}
	// r: SHORT_ALOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6F00; // p.r = 111
		closure_r(p, c);
	}
}

private void label_INT_ASTORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: INT_ASTORE(INT_ADD(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_ADD_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x1A; // p.stm = 26
		}
	}
	if (	// stm: INT_ASTORE(INT_ADD(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_ADD_opcode && 
		lchild.child2.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLR(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x1B; // p.stm = 27
		}
	}
	if (	// stm: INT_ASTORE(INT_SUB(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SUB_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x1E; // p.stm = 30
		}
	}
	if (	// stm: INT_ASTORE(INT_SUB(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SUB_opcode && 
		lchild.child2.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLR(p), 27);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x1F; // p.stm = 31
		}
	}
	if (	// stm: INT_ASTORE(INT_NEG(INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_NEG_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x21; // p.stm = 33
		}
	}
	if (	// stm: INT_ASTORE(INT_SHL(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ARRAY_ADDRESS_EQUAL(P(p), PLL(p), VLRR(p) == 31 ? 27 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x24; // p.stm = 36
		}
	}
	if (	// stm: INT_ASTORE(INT_SHL(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ARRAY_ADDRESS_EQUAL(P(p), PLL(p), VLR(p) == 31 ? 17 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x25; // p.stm = 37
		}
	}
	if (	// stm: INT_ASTORE(INT_SHR(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SHR_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ARRAY_ADDRESS_EQUAL(P(p), PLL(p), VLRR(p) == 31 ? 27 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x28; // p.stm = 40
		}
	}
	if (	// stm: INT_ASTORE(INT_SHR(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SHR_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ARRAY_ADDRESS_EQUAL(P(p), PLL(p), VLR(p) == 31 ? 17 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x29; // p.stm = 41
		}
	}
	if (	// stm: INT_ASTORE(INT_USHR(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ARRAY_ADDRESS_EQUAL(P(p), PLL(p), VLRR(p) == 31 ? 27 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x2C; // p.stm = 44
		}
	}
	if (	// stm: INT_ASTORE(INT_USHR(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ARRAY_ADDRESS_EQUAL(P(p), PLL(p), VLR(p) == 31 ? 17 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x2D; // p.stm = 45
		}
	}
	if (	// stm: INT_ASTORE(INT_AND(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_AND_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x30; // p.stm = 48
		}
	}
	if (	// stm: INT_ASTORE(INT_AND(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_AND_opcode && 
		lchild.child2.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLR(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x31; // p.stm = 49
		}
	}
	if (	// stm: INT_ASTORE(INT_OR(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_OR_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x34; // p.stm = 52
		}
	}
	if (	// stm: INT_ASTORE(INT_OR(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_OR_opcode && 
		lchild.child2.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLR(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x35; // p.stm = 53
		}
	}
	if (	// stm: INT_ASTORE(INT_XOR(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_XOR_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x38; // p.stm = 56
		}
	}
	if (	// stm: INT_ASTORE(INT_XOR(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_XOR_opcode && 
		lchild.child2.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLR(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x39; // p.stm = 57
		}
	}
	if (	// stm: INT_ASTORE(INT_NOT(INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_NOT_opcode && 
		lchild.child1.getOpcode() == INT_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x3B; // p.stm = 59
		}
	}
	if (	// stm: INT_ASTORE(LONG_2INT(r),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == LONG_2INT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x43; // p.stm = 67
		}
	}
	if (	// stm: INT_ASTORE(riv,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x60; // p.stm = 96
		}
	}
}

private void label_LONG_ASTORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: LONG_ASTORE(r,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 30;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x63; // p.stm = 99
		}
	}
	if (	// stm: LONG_ASTORE(LONG_CONSTANT,OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == LONG_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 26;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x64; // p.stm = 100
		}
	}
	if (	// stm: LONG_ASTORE(load64,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_load64 + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 32;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x8B; // p.stm = 139
		}
	}
}

private void label_FLOAT_ASTORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: FLOAT_ASTORE(r,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 17;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x83; // p.stm = 131
		}
	}
}

private void label_DOUBLE_ASTORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: DOUBLE_ASTORE(r,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 17;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x81; // p.stm = 129
		}
	}
}

private void label_BYTE_ASTORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: BYTE_ASTORE(BOOLEAN_NOT(UBYTE_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == BOOLEAN_NOT_opcode && 
		lchild.child1.getOpcode() == UBYTE_ALOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ARRAY_ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x15; // p.stm = 21
		}
	}
	if (	// stm: BYTE_ASTORE(boolcmp,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x17; // p.stm = 23
		}
	}
	if (	// stm: BYTE_ASTORE(INT_2BYTE(r),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_2BYTE_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x3D; // p.stm = 61
		}
	}
	if (	// stm: BYTE_ASTORE(riv,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x54; // p.stm = 84
		}
	}
	if (	// stm: BYTE_ASTORE(load8,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_load8 + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 25;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x55; // p.stm = 85
		}
	}
}

private void label_SHORT_ASTORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: SHORT_ASTORE(INT_2USHORT(r),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_2USHORT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x3F; // p.stm = 63
		}
	}
	if (	// stm: SHORT_ASTORE(INT_2SHORT(r),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_2SHORT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x41; // p.stm = 65
		}
	}
	if (	// stm: SHORT_ASTORE(riv,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x58; // p.stm = 88
		}
	}
	if (	// stm: SHORT_ASTORE(load16,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_load16 + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 25;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x59; // p.stm = 89
		}
	}
}

private void label_INT_IFCMP(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: INT_IFCMP(ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isZERO(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x44; // p.stm = 68
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(lchild.child2.child1).cost_address1scaledreg + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isZERO(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x45; // p.stm = 69
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_address1scaledreg + STATE(lchild.child2.child1).cost_r + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isZERO(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x46; // p.stm = 70
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_address1scaledreg + STATE(lchild.child2.child1).cost_address1reg + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isZERO(VR(p),54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x47; // p.stm = 71
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_address1reg + STATE(lchild.child2.child1).cost_address1scaledreg + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isZERO(VR(p),54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x48; // p.stm = 72
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child1.getOpcode() == INT_CONSTANT_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_address + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isZERO(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x49; // p.stm = 73
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child2.child1).cost_address + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isZERO(VR(p),54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x4A; // p.stm = 74
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isONE(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x4B; // p.stm = 75
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(lchild.child2.child1).cost_address1scaledreg + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isONE(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x4C; // p.stm = 76
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_address1scaledreg + STATE(lchild.child2.child1).cost_r + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isONE(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x4D; // p.stm = 77
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_address1scaledreg + STATE(lchild.child2.child1).cost_address1reg + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isONE(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x4E; // p.stm = 78
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_address1reg + STATE(lchild.child2.child1).cost_address1scaledreg + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isONE(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x4F; // p.stm = 79
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child1.getOpcode() == INT_CONSTANT_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_address + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isONE(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x50; // p.stm = 80
		}
	}
	if (	// stm: INT_IFCMP(ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		lchild.getOpcode() == ATTEMPT_INT_opcode && 
		lchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		lchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		lchild.child2.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child2.child1).cost_address + STATE(lchild.child2.child2.child1).cost_riv + STATE(lchild.child2.child2.child2).cost_riv + isONE(VR(p), 54);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x51; // p.stm = 81
		}
	}
	// stm: INT_IFCMP(r,riv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_riv + 26;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x65; // p.stm = 101
	}
	if (	// stm: INT_IFCMP(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + (VR(p) == 0 && CMP_TO_TEST(IfCmp.getCond(P(p))) ? 24:INFINITE);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x66; // p.stm = 102
		}
	}
	if (	// stm: INT_IFCMP(load8,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load8 + FITS(IfCmp.getVal2(P(p)), 8, 28);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x67; // p.stm = 103
		}
	}
	// stm: INT_IFCMP(uload8,r)
	c = STATE(lchild).cost_uload8 + STATE(rchild).cost_r + 28;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x68; // p.stm = 104
	}
	// stm: INT_IFCMP(r,uload8)
	c = STATE(lchild).cost_r + STATE(rchild).cost_uload8 + 28;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x69; // p.stm = 105
	}
	if (	// stm: INT_IFCMP(sload16,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_sload16 + FITS(IfCmp.getVal2(P(p)), 8, 28);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x6A; // p.stm = 106
		}
	}
	// stm: INT_IFCMP(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 28;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x6B; // p.stm = 107
	}
	// stm: INT_IFCMP(r,load32)
	c = STATE(lchild).cost_r + STATE(rchild).cost_load32 + 28;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x6C; // p.stm = 108
	}
	if (	// stm: INT_IFCMP(boolcmp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + ((VR(p) == 0 && IfCmp.getCond(P(p)).isNOT_EQUAL()) || (VR(p) == 1 && IfCmp.getCond(P(p)).isEQUAL()) ? 13:INFINITE);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x6D; // p.stm = 109
		}
	}
	if (	// stm: INT_IFCMP(boolcmp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + ((VR(p) == 0 && IfCmp.getCond(P(p)).isEQUAL()) || (VR(p) == 1 && IfCmp.getCond(P(p)).isNOT_EQUAL()) ? 13:INFINITE);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x6E; // p.stm = 110
		}
	}
	if (	// stm: INT_IFCMP(cz,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_cz + isZERO(VR(p), 11);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x6F; // p.stm = 111
		}
	}
	if (	// stm: INT_IFCMP(szp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_szp + (VR(p) == 0 && EQ_NE(IfCmp.getCond(P(p)))?11:INFINITE);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x70; // p.stm = 112
		}
	}
	if (	// stm: INT_IFCMP(bittest,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_bittest + ((VR(p) == 0 || VR(p) == 1) && EQ_NE(IfCmp.getCond(P(p))) ? 11 : INFINITE);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x71; // p.stm = 113
		}
	}
}

private void label_INT_IFCMP2(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// stm: INT_IFCMP2(r,riv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_riv + 26;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x72; // p.stm = 114
	}
	// stm: INT_IFCMP2(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 28;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x73; // p.stm = 115
	}
	// stm: INT_IFCMP2(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 28;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x74; // p.stm = 116
	}
}

private void label_LONG_IFCMP(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// stm: LONG_IFCMP(rlv,rlv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_rlv + 30;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x75; // p.stm = 117
	}
}

private void label_FLOAT_IFCMP(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// stm: FLOAT_IFCMP(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x85; // p.stm = 133
	}
	// stm: FLOAT_IFCMP(r,float_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_float_load + 15;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x86; // p.stm = 134
	}
	// stm: FLOAT_IFCMP(float_load,r)
	c = STATE(lchild).cost_float_load + STATE(rchild).cost_r + 15;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x87; // p.stm = 135
	}
}

private void label_DOUBLE_IFCMP(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// stm: DOUBLE_IFCMP(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x88; // p.stm = 136
	}
	// stm: DOUBLE_IFCMP(r,double_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_double_load + 15;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x89; // p.stm = 137
	}
	// stm: DOUBLE_IFCMP(double_load,r)
	c = STATE(lchild).cost_double_load + STATE(rchild).cost_r + 15;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x8A; // p.stm = 138
	}
}

private void label_UNINT_BEGIN(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: UNINT_BEGIN
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x3; // p.stm = 3
	}
}

private void label_UNINT_END(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: UNINT_END
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x4; // p.stm = 4
	}
}

private void label_READ_CEILING(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: READ_CEILING
	if (11 < p.cost_stm) {
		p.cost_stm = (char)(11);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x79; // p.stm = 121
	}
}

private void label_WRITE_FLOOR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: WRITE_FLOOR
	if (11 < p.cost_stm) {
		p.cost_stm = (char)(11);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x78; // p.stm = 120
	}
}

private void label_NOP(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: NOP
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0xA; // p.stm = 10
	}
}

private void label_INT_MOVE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// address1reg: INT_MOVE(r)
	c = STATE(lchild).cost_r + 0;
	if (c < p.cost_address1reg) {
		p.cost_address1reg = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFFF8) | 0x2; // p.address1reg = 2
		closure_address1reg(p, c);
	}
	// r: INT_MOVE(address)
	c = STATE(lchild).cost_address + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x900; // p.r = 9
		closure_r(p, c);
	}
	// r: INT_MOVE(riv)
	c = STATE(lchild).cost_riv + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6300; // p.r = 99
		closure_r(p, c);
	}
	// czr: INT_MOVE(czr)
	c = STATE(lchild).cost_czr + 11;
	if (c < p.cost_czr) {
		p.cost_czr = (char)(c);
		p.word0 = (p.word0 & 0xFFF8FFFF) | 0x70000; // p.czr = 7
		closure_czr(p, c);
	}
	// cz: INT_MOVE(cz)
	c = STATE(lchild).cost_cz + 0;
	if (c < p.cost_cz) {
		p.cost_cz = (char)(c);
		p.word0 = (p.word0 & 0xFFE7FFFF) | 0x100000; // p.cz = 2
	}
	// szpr: INT_MOVE(szpr)
	c = STATE(lchild).cost_szpr + 11;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x2C00000; // p.szpr = 22
		closure_szpr(p, c);
	}
	// szp: INT_MOVE(szp)
	c = STATE(lchild).cost_szp + 0;
	if (c < p.cost_szp) {
		p.cost_szp = (char)(c);
		p.word0 = (p.word0 & 0xE3FFFFFF) | 0x14000000; // p.szp = 5
	}
	// address1reg: INT_MOVE(address1reg)
	c = STATE(lchild).cost_address1reg + 0;
	if (c < p.cost_address1reg) {
		p.cost_address1reg = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFFF8) | 0x4; // p.address1reg = 4
		closure_address1reg(p, c);
	}
	// address1scaledreg: INT_MOVE(address1scaledreg)
	c = STATE(lchild).cost_address1scaledreg + 0;
	if (c < p.cost_address1scaledreg) {
		p.cost_address1scaledreg = (char)(c);
		p.word1 = (p.word1 & 0x8FFFFFFF) | 0x40000000; // p.address1scaledreg = 4
		closure_address1scaledreg(p, c);
	}
	// address: INT_MOVE(address)
	c = STATE(lchild).cost_address + 0;
	if (c < p.cost_address) {
		p.cost_address = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFF87) | 0x40; // p.address = 8
	}
	// sload8: INT_MOVE(sload8)
	c = STATE(lchild).cost_sload8 + 0;
	if (c < p.cost_sload8) {
		p.cost_sload8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFF9F) | 0x60; // p.sload8 = 3
		closure_sload8(p, c);
	}
	// uload8: INT_MOVE(uload8)
	c = STATE(lchild).cost_uload8 + 0;
	if (c < p.cost_uload8) {
		p.cost_uload8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFC7F) | 0x180; // p.uload8 = 3
		closure_uload8(p, c);
	}
	// load8: INT_MOVE(load8)
	c = STATE(lchild).cost_load8 + 0;
	if (c < p.cost_load8) {
		p.cost_load8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFF3FF) | 0xC00; // p.load8 = 3
		closure_load8(p, c);
	}
	// sload16: INT_MOVE(sload16)
	c = STATE(lchild).cost_sload16 + 0;
	if (c < p.cost_sload16) {
		p.cost_sload16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFCFFF) | 0x3000; // p.sload16 = 3
		closure_sload16(p, c);
	}
	// uload16: INT_MOVE(uload16)
	c = STATE(lchild).cost_uload16 + 0;
	if (c < p.cost_uload16) {
		p.cost_uload16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFF3FFF) | 0xC000; // p.uload16 = 3
		closure_uload16(p, c);
	}
	// load16: INT_MOVE(load16)
	c = STATE(lchild).cost_load16 + 0;
	if (c < p.cost_load16) {
		p.cost_load16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFCFFFF) | 0x30000; // p.load16 = 3
		closure_load16(p, c);
	}
	// load32: INT_MOVE(load32)
	c = STATE(lchild).cost_load32 + 0;
	if (c < p.cost_load32) {
		p.cost_load32 = (char)(c);
		p.word1 = (p.word1 & 0xFFE3FFFF) | 0x140000; // p.load32 = 5
		closure_load32(p, c);
	}
}

private void label_LONG_MOVE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// load64: LONG_MOVE(load64)
	c = STATE(lchild).cost_load64 + 0;
	if (c < p.cost_load64) {
		p.cost_load64 = (char)(c);
		p.word1 = (p.word1 & 0xF1FFFFFF) | 0x6000000; // p.load64 = 3
	}
	// r: LONG_MOVE(r)
	c = STATE(lchild).cost_r + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6400; // p.r = 100
		closure_r(p, c);
	}
	if (	// r: LONG_MOVE(LONG_CONSTANT)
		lchild.getOpcode() == LONG_CONSTANT_opcode 
	) {
		c = 21;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x6500; // p.r = 101
			closure_r(p, c);
		}
	}
}

private void label_FLOAT_MOVE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: FLOAT_MOVE(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB400; // p.r = 180
		closure_r(p, c);
	}
}

private void label_DOUBLE_MOVE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: DOUBLE_MOVE(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB500; // p.r = 181
		closure_r(p, c);
	}
}

private void label_GUARD_MOVE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// r: GUARD_MOVE
	if (11 < p.cost_r) {
		p.cost_r = (char)(11);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA00; // p.r = 10
		closure_r(p, 11);
	}
}

private void label_GUARD_COMBINE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// r: GUARD_COMBINE
	if (11 < p.cost_r) {
		p.cost_r = (char)(11);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB00; // p.r = 11
		closure_r(p, 11);
	}
}

private void label_INT_ADD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// address1reg: INT_ADD(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + 0;
		if (c < p.cost_address1reg) {
			p.cost_address1reg = (char)(c);
			p.word2 = (p.word2 & 0xFFFFFFF8) | 0x1; // p.address1reg = 1
			closure_address1reg(p, c);
		}
	}
	// address: INT_ADD(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 0;
	if (c < p.cost_address) {
		p.cost_address = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFF87) | 0x10; // p.address = 2
	}
	if (	// address1reg: INT_ADD(address1reg,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_address1reg + 0;
		if (c < p.cost_address1reg) {
			p.cost_address1reg = (char)(c);
			p.word2 = (p.word2 & 0xFFFFFFF8) | 0x3; // p.address1reg = 3
			closure_address1reg(p, c);
		}
	}
	if (	// address1scaledreg: INT_ADD(address1scaledreg,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_address1scaledreg + 0;
		if (c < p.cost_address1scaledreg) {
			p.cost_address1scaledreg = (char)(c);
			p.word1 = (p.word1 & 0x8FFFFFFF) | 0x30000000; // p.address1scaledreg = 3
			closure_address1scaledreg(p, c);
		}
	}
	// address: INT_ADD(r,address1scaledreg)
	c = STATE(lchild).cost_r + STATE(rchild).cost_address1scaledreg + 0;
	if (c < p.cost_address) {
		p.cost_address = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFF87) | 0x18; // p.address = 3
	}
	// address: INT_ADD(address1scaledreg,r)
	c = STATE(lchild).cost_address1scaledreg + STATE(rchild).cost_r + 0;
	if (c < p.cost_address) {
		p.cost_address = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFF87) | 0x20; // p.address = 4
	}
	if (	// address: INT_ADD(address1scaledreg,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_address1scaledreg + 0;
		if (c < p.cost_address) {
			p.cost_address = (char)(c);
			p.word2 = (p.word2 & 0xFFFFFF87) | 0x28; // p.address = 5
		}
	}
	// address: INT_ADD(address1scaledreg,address1reg)
	c = STATE(lchild).cost_address1scaledreg + STATE(rchild).cost_address1reg + 0;
	if (c < p.cost_address) {
		p.cost_address = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFF87) | 0x30; // p.address = 6
	}
	// address: INT_ADD(address1reg,address1scaledreg)
	c = STATE(lchild).cost_address1reg + STATE(rchild).cost_address1scaledreg + 0;
	if (c < p.cost_address) {
		p.cost_address = (char)(c);
		p.word2 = (p.word2 & 0xFFFFFF87) | 0x38; // p.address = 7
	}
	// r: INT_ADD(address1scaledreg,r)
	c = STATE(lchild).cost_address1scaledreg + STATE(rchild).cost_r + 11;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x400; // p.r = 4
		closure_r(p, c);
	}
	// r: INT_ADD(r,address1scaledreg)
	c = STATE(lchild).cost_r + STATE(rchild).cost_address1scaledreg + 11;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x500; // p.r = 5
		closure_r(p, c);
	}
	// r: INT_ADD(address1scaledreg,address1reg)
	c = STATE(lchild).cost_address1scaledreg + STATE(rchild).cost_address1reg + 11;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x600; // p.r = 6
		closure_r(p, c);
	}
	// r: INT_ADD(address1reg,address1scaledreg)
	c = STATE(lchild).cost_address1reg + STATE(rchild).cost_address1scaledreg + 11;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x700; // p.r = 7
		closure_r(p, c);
	}
	if (	// r: INT_ADD(address,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_address + 11;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x800; // p.r = 8
			closure_r(p, c);
		}
	}
	// czr: INT_ADD(r,riv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_riv + 13;
	if (c < p.cost_czr) {
		p.cost_czr = (char)(c);
		p.word0 = (p.word0 & 0xFFF8FFFF) | 0x10000; // p.czr = 1
		closure_czr(p, c);
	}
	// r: INT_ADD(r,riv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_riv + (!Binary.getResult(P(p)).similar(Binary.getVal1(P(p))) && !Binary.getResult(P(p)).similar(Binary.getVal2(P(p))) ? 11 : INFINITE);
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x2E00; // p.r = 46
		closure_r(p, c);
	}
	// czr: INT_ADD(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 15;
	if (c < p.cost_czr) {
		p.cost_czr = (char)(c);
		p.word0 = (p.word0 & 0xFFF8FFFF) | 0x20000; // p.czr = 2
		closure_czr(p, c);
	}
	// czr: INT_ADD(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 15;
	if (c < p.cost_czr) {
		p.cost_czr = (char)(c);
		p.word0 = (p.word0 & 0xFFF8FFFF) | 0x30000; // p.czr = 3
		closure_czr(p, c);
	}
}

private void label_LONG_ADD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_ADD(r,rlv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_rlv + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4200; // p.r = 66
		closure_r(p, c);
	}
}

private void label_FLOAT_ADD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: FLOAT_ADD(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9800; // p.r = 152
		closure_r(p, c);
	}
	// r: FLOAT_ADD(r,float_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_float_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9900; // p.r = 153
		closure_r(p, c);
	}
	// r: FLOAT_ADD(float_load,r)
	c = STATE(lchild).cost_float_load + STATE(rchild).cost_r + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9A00; // p.r = 154
		closure_r(p, c);
	}
}

private void label_DOUBLE_ADD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: DOUBLE_ADD(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9B00; // p.r = 155
		closure_r(p, c);
	}
	// r: DOUBLE_ADD(r,double_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_double_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9C00; // p.r = 156
		closure_r(p, c);
	}
	// r: DOUBLE_ADD(double_load,r)
	c = STATE(lchild).cost_double_load + STATE(rchild).cost_r + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9D00; // p.r = 157
		closure_r(p, c);
	}
}

private void label_INT_SUB(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// czr: INT_SUB(riv,r)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_r + 13;
	if (c < p.cost_czr) {
		p.cost_czr = (char)(c);
		p.word0 = (p.word0 & 0xFFF8FFFF) | 0x40000; // p.czr = 4
		closure_czr(p, c);
	}
	// r: INT_SUB(riv,r)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_r + (Binary.getResult(P(p)).similar(Binary.getVal2(P(p))) ? 13-2 : INFINITE);
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x2F00; // p.r = 47
		closure_r(p, c);
	}
	// r: INT_SUB(load32,r)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_r + (Binary.getResult(P(p)).similar(Binary.getVal2(P(p))) ? 15-2 : INFINITE);
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x3000; // p.r = 48
		closure_r(p, c);
	}
	// czr: INT_SUB(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 15;
	if (c < p.cost_czr) {
		p.cost_czr = (char)(c);
		p.word0 = (p.word0 & 0xFFF8FFFF) | 0x50000; // p.czr = 5
		closure_czr(p, c);
	}
	// czr: INT_SUB(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 15;
	if (c < p.cost_czr) {
		p.cost_czr = (char)(c);
		p.word0 = (p.word0 & 0xFFF8FFFF) | 0x60000; // p.czr = 6
		closure_czr(p, c);
	}
}

private void label_LONG_SUB(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_SUB(rlv,rlv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_rlv + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4300; // p.r = 67
		closure_r(p, c);
	}
}

private void label_FLOAT_SUB(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: FLOAT_SUB(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9E00; // p.r = 158
		closure_r(p, c);
	}
	// r: FLOAT_SUB(r,float_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_float_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9F00; // p.r = 159
		closure_r(p, c);
	}
}

private void label_DOUBLE_SUB(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: DOUBLE_SUB(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA000; // p.r = 160
		closure_r(p, c);
	}
	// r: DOUBLE_SUB(r,double_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_double_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA100; // p.r = 161
		closure_r(p, c);
	}
}

private void label_INT_MUL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: INT_MUL(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x3100; // p.r = 49
		closure_r(p, c);
	}
	// r: INT_MUL(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x3200; // p.r = 50
		closure_r(p, c);
	}
	// r: INT_MUL(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x3300; // p.r = 51
		closure_r(p, c);
	}
}

private void label_LONG_MUL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_MUL(rlv,rlv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_rlv + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4400; // p.r = 68
		closure_r(p, c);
	}
}

private void label_FLOAT_MUL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: FLOAT_MUL(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA200; // p.r = 162
		closure_r(p, c);
	}
	// r: FLOAT_MUL(r,float_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_float_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA300; // p.r = 163
		closure_r(p, c);
	}
	// r: FLOAT_MUL(float_load,r)
	c = STATE(lchild).cost_float_load + STATE(rchild).cost_r + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA400; // p.r = 164
		closure_r(p, c);
	}
}

private void label_DOUBLE_MUL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: DOUBLE_MUL(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA500; // p.r = 165
		closure_r(p, c);
	}
	// r: DOUBLE_MUL(r,double_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_double_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA600; // p.r = 166
		closure_r(p, c);
	}
	// r: DOUBLE_MUL(double_load,r)
	c = STATE(lchild).cost_double_load + STATE(rchild).cost_r + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA700; // p.r = 167
		closure_r(p, c);
	}
}

private void label_INT_DIV(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: INT_DIV(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 52;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x3400; // p.r = 52
		closure_r(p, c);
	}
	// r: INT_DIV(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 55;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x3500; // p.r = 53
		closure_r(p, c);
	}
}

private void label_FLOAT_DIV(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: FLOAT_DIV(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA800; // p.r = 168
		closure_r(p, c);
	}
	// r: FLOAT_DIV(r,float_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_float_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xA900; // p.r = 169
		closure_r(p, c);
	}
}

private void label_DOUBLE_DIV(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: DOUBLE_DIV(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xAA00; // p.r = 170
		closure_r(p, c);
	}
	// r: DOUBLE_DIV(r,double_load)
	c = STATE(lchild).cost_r + STATE(rchild).cost_double_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xAB00; // p.r = 171
		closure_r(p, c);
	}
}

private void label_INT_REM(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: INT_REM(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 52;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x3600; // p.r = 54
		closure_r(p, c);
	}
	// r: INT_REM(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 55;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x3700; // p.r = 55
		closure_r(p, c);
	}
}

private void label_FLOAT_REM(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: FLOAT_REM(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB000; // p.r = 176
		closure_r(p, c);
	}
}

private void label_DOUBLE_REM(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: DOUBLE_REM(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB100; // p.r = 177
		closure_r(p, c);
	}
}

private void label_INT_NEG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// szpr: INT_NEG(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x200000; // p.szpr = 1
		closure_szpr(p, c);
	}
}

private void label_LONG_NEG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: LONG_NEG(r)
	c = STATE(lchild).cost_r + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4500; // p.r = 69
		closure_r(p, c);
	}
}

private void label_FLOAT_NEG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: FLOAT_NEG(r)
	c = STATE(lchild).cost_r + 26;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xAC00; // p.r = 172
		closure_r(p, c);
	}
}

private void label_DOUBLE_NEG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: DOUBLE_NEG(r)
	c = STATE(lchild).cost_r + 26;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xAD00; // p.r = 173
		closure_r(p, c);
	}
}

private void label_FLOAT_SQRT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: FLOAT_SQRT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xAE00; // p.r = 174
		closure_r(p, c);
	}
}

private void label_DOUBLE_SQRT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: DOUBLE_SQRT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xAF00; // p.r = 175
		closure_r(p, c);
	}
}

private void label_INT_SHL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// address1scaledreg: INT_SHL(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + LEA_SHIFT(Binary.getVal2(P(p)), 0);
		if (c < p.cost_address1scaledreg) {
			p.cost_address1scaledreg = (char)(c);
			p.word1 = (p.word1 & 0x8FFFFFFF) | 0x20000000; // p.address1scaledreg = 2
			closure_address1scaledreg(p, c);
		}
	}
	if (	// szpr: INT_SHL(riv,INT_AND(r,INT_CONSTANT))
		rchild.getOpcode() == INT_AND_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_r + (VRR(p) == 31 ? 23 : INFINITE);
		if (c < p.cost_szpr) {
			p.cost_szpr = (char)(c);
			p.word0 = (p.word0 & 0xFC1FFFFF) | 0x400000; // p.szpr = 2
			closure_szpr(p, c);
		}
	}
	// szpr: INT_SHL(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 23;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x600000; // p.szpr = 3
		closure_szpr(p, c);
	}
	if (	// szpr: INT_SHL(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + 13;
		if (c < p.cost_szpr) {
			p.cost_szpr = (char)(c);
			p.word0 = (p.word0 & 0xFC1FFFFF) | 0x800000; // p.szpr = 4
			closure_szpr(p, c);
		}
	}
	if (	// r: INT_SHL(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + (!Binary.getResult(P(p)).similar(Binary.getVal1(P(p))) && (Binary.getVal2(P(p)).asIntConstant().value & 0x1f) <= 3 ? 11 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x3800; // p.r = 56
			closure_r(p, c);
		}
	}
	if (	// szpr: INT_SHL(INT_SHR(r,INT_CONSTANT),INT_CONSTANT)
		lchild.getOpcode() == INT_SHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + ((VR(p) == VLR(p)) ? 23 : INFINITE);
		if (c < p.cost_szpr) {
			p.cost_szpr = (char)(c);
			p.word0 = (p.word0 & 0xFC1FFFFF) | 0xA00000; // p.szpr = 5
			closure_szpr(p, c);
		}
	}
}

private void label_LONG_SHL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_SHL(rlv,riv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_riv + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4600; // p.r = 70
		closure_r(p, c);
	}
	if (	// r: LONG_SHL(rlv,INT_AND(riv,INT_CONSTANT))
		rchild.getOpcode() == INT_AND_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_rlv + STATE(rchild.child1).cost_riv + 20;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x4700; // p.r = 71
			closure_r(p, c);
		}
	}
	if (	// r: LONG_SHL(INT_2LONG(r),INT_CONSTANT)
		lchild.getOpcode() == INT_2LONG_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + (VR(p) == 32 ? 23 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x5900; // p.r = 89
			closure_r(p, c);
		}
	}
	if (	// r: LONG_SHL(INT_2LONG(load64),INT_CONSTANT)
		lchild.getOpcode() == INT_2LONG_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load64 + (VR(p) == 32 ? 23 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x5A00; // p.r = 90
			closure_r(p, c);
		}
	}
}

private void label_INT_SHR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// szpr: INT_SHR(riv,INT_AND(r,INT_CONSTANT))
		rchild.getOpcode() == INT_AND_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_r + (VRR(p) == 31 ? 23 : INFINITE);
		if (c < p.cost_szpr) {
			p.cost_szpr = (char)(c);
			p.word0 = (p.word0 & 0xFC1FFFFF) | 0xC00000; // p.szpr = 6
			closure_szpr(p, c);
		}
	}
	// szpr: INT_SHR(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 23;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0xE00000; // p.szpr = 7
		closure_szpr(p, c);
	}
	if (	// szpr: INT_SHR(riv,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_riv + 13;
		if (c < p.cost_szpr) {
			p.cost_szpr = (char)(c);
			p.word0 = (p.word0 & 0xFC1FFFFF) | 0x1000000; // p.szpr = 8
			closure_szpr(p, c);
		}
	}
}

private void label_LONG_SHR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_SHR(rlv,riv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_riv + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4800; // p.r = 72
		closure_r(p, c);
	}
	if (	// r: LONG_SHR(rlv,INT_AND(riv,INT_CONSTANT))
		rchild.getOpcode() == INT_AND_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_rlv + STATE(rchild.child1).cost_riv + 20;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x4900; // p.r = 73
			closure_r(p, c);
		}
	}
}

private void label_INT_USHR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// szpr: INT_USHR(riv,INT_AND(r,INT_CONSTANT))
		rchild.getOpcode() == INT_AND_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_r + (VRR(p) == 31 ? 23 : INFINITE);
		if (c < p.cost_szpr) {
			p.cost_szpr = (char)(c);
			p.word0 = (p.word0 & 0xFC1FFFFF) | 0x1200000; // p.szpr = 9
			closure_szpr(p, c);
		}
	}
	// szpr: INT_USHR(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 23;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x1400000; // p.szpr = 10
		closure_szpr(p, c);
	}
	if (	// szpr: INT_USHR(riv,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_riv + 13;
		if (c < p.cost_szpr) {
			p.cost_szpr = (char)(c);
			p.word0 = (p.word0 & 0xFC1FFFFF) | 0x1600000; // p.szpr = 11
			closure_szpr(p, c);
		}
	}
	if (	// r: INT_USHR(INT_SHL(load8_16_32,INT_CONSTANT),INT_CONSTANT)
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load8_16_32 + (VR(p) == 24 && VLLR(p) == 24 ? 15 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x6D00; // p.r = 109
			closure_r(p, c);
		}
	}
	if (	// r: INT_USHR(INT_SHL(load16_32,INT_CONSTANT),INT_CONSTANT)
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load16_32 + (VR(p) == 16 && VLR(p) == 16 ? 15 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x7500; // p.r = 117
			closure_r(p, c);
		}
	}
}

private void label_LONG_USHR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_USHR(rlv,riv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_riv + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4A00; // p.r = 74
		closure_r(p, c);
	}
	if (	// r: LONG_USHR(rlv,INT_AND(riv,INT_CONSTANT))
		rchild.getOpcode() == INT_AND_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_rlv + STATE(rchild.child1).cost_riv + 20;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x4B00; // p.r = 75
			closure_r(p, c);
		}
	}
}

private void label_INT_AND(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// szpr: INT_AND(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 13;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x1800000; // p.szpr = 12
		closure_szpr(p, c);
	}
	// szp: INT_AND(r,riv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_riv + 11;
	if (c < p.cost_szp) {
		p.cost_szp = (char)(c);
		p.word0 = (p.word0 & 0xE3FFFFFF) | 0x8000000; // p.szp = 2
	}
	// szpr: INT_AND(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 15;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x1A00000; // p.szpr = 13
		closure_szpr(p, c);
	}
	// szpr: INT_AND(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 15;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x1C00000; // p.szpr = 14
		closure_szpr(p, c);
	}
	// szp: INT_AND(load8_16_32,riv)
	c = STATE(lchild).cost_load8_16_32 + STATE(rchild).cost_riv + 11;
	if (c < p.cost_szp) {
		p.cost_szp = (char)(c);
		p.word0 = (p.word0 & 0xE3FFFFFF) | 0xC000000; // p.szp = 3
	}
	// szp: INT_AND(riv,load8_16_32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load8_16_32 + 11;
	if (c < p.cost_szp) {
		p.cost_szp = (char)(c);
		p.word0 = (p.word0 & 0xE3FFFFFF) | 0x10000000; // p.szp = 4
	}
	if (	// uload8: INT_AND(load8_16_32,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load8_16_32 + (VR(p) == 0xff ? 0 : INFINITE);
		if (c < p.cost_uload8) {
			p.cost_uload8 = (char)(c);
			p.word1 = (p.word1 & 0xFFFFFC7F) | 0x200; // p.uload8 = 4
			closure_uload8(p, c);
		}
	}
	if (	// r: INT_AND(load8_16_32,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load8_16_32 + (VR(p) == 0xff ? 15 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x6B00; // p.r = 107
			closure_r(p, c);
		}
	}
	if (	// r: INT_AND(load16_32,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load16_32 + (VR(p) == 0xffff ? 15 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x7200; // p.r = 114
			closure_r(p, c);
		}
	}
	if (	// bittest: INT_AND(INT_USHR(r,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(lchild.child2.child1).cost_r + ((VR(p) == 1) && (VLRR(p) == 31) ? 13:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x800; // p.bittest = 1
		}
	}
	if (	// bittest: INT_AND(INT_USHR(load32,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load32 + STATE(lchild.child2.child1).cost_r + (VR(p) == 1 ? 31:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x1000; // p.bittest = 2
		}
	}
	if (	// bittest: INT_AND(INT_USHR(r,INT_CONSTANT),INT_CONSTANT)
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + ((VR(p) == 1) && (VLR(p) <= 31) ? 13:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x1800; // p.bittest = 3
		}
	}
	if (	// bittest: INT_AND(INT_SHR(r,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
		lchild.getOpcode() == INT_SHR_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(lchild.child2.child1).cost_r + ((VR(p) == 1) && (VLRR(p) == 31) ? 13:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x2000; // p.bittest = 4
		}
	}
	if (	// bittest: INT_AND(INT_SHR(load32,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
		lchild.getOpcode() == INT_SHR_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load32 + STATE(lchild.child2.child1).cost_r + (VR(p) == 1 ? 31:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x2800; // p.bittest = 5
		}
	}
	if (	// bittest: INT_AND(INT_SHR(r,INT_CONSTANT),INT_CONSTANT)
		lchild.getOpcode() == INT_SHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + ((VR(p) == 1) && (VLR(p) <= 31) ? 13:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x3000; // p.bittest = 6
		}
	}
	if (	// bittest: INT_AND(INT_SHL(INT_CONSTANT,INT_AND(riv,INT_CONSTANT)),r)
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child2.child1).cost_riv + STATE(rchild).cost_r + ((VLL(p) == 1) && (VLRR(p) == 31)? 13:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x3800; // p.bittest = 7
		}
	}
	if (	// bittest: INT_AND(INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)),load32)
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child2.child1).cost_r + STATE(rchild).cost_load32 + (VLL(p) == 1 ? 31:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x4000; // p.bittest = 8
		}
	}
	if (	// bittest: INT_AND(r,INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)))
		rchild.getOpcode() == INT_SHL_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == INT_AND_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child2.child1).cost_r + ((VRL(p) == 1) && (VRRR(p) == 31) ? 13:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x4800; // p.bittest = 9
		}
	}
	if (	// bittest: INT_AND(load32,INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)))
		rchild.getOpcode() == INT_SHL_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == INT_AND_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load32 + STATE(rchild.child2.child1).cost_r + (VRL(p) == 1 ? 31:INFINITE);
		if (c < p.cost_bittest) {
			p.cost_bittest = (char)(c);
			p.word2 = (p.word2 & 0xFFFF87FF) | 0x5000; // p.bittest = 10
		}
	}
}

private void label_LONG_AND(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_AND(rlv,rlv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_rlv + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4C00; // p.r = 76
		closure_r(p, c);
	}
	if (	// r: LONG_AND(INT_2LONG(r),LONG_CONSTANT)
		lchild.getOpcode() == INT_2LONG_opcode && 
		rchild.getOpcode() == LONG_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + ((Binary.getVal2(P(p)).asLongConstant().upper32() == 0) && (Binary.getVal2(P(p)).asLongConstant().lower32() == -1)? 23 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x5700; // p.r = 87
			closure_r(p, c);
		}
	}
	if (	// r: LONG_AND(INT_2LONG(load32),LONG_CONSTANT)
		lchild.getOpcode() == INT_2LONG_opcode && 
		rchild.getOpcode() == LONG_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load32 + ((Binary.getVal2(P(p)).asLongConstant().upper32() == 0) && (Binary.getVal2(P(p)).asLongConstant().lower32() == -1)? 28 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x5800; // p.r = 88
			closure_r(p, c);
		}
	}
}

private void label_INT_OR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// r: INT_OR(INT_SHL(r,INT_CONSTANT),INT_USHR(r,INT_CONSTANT))
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_USHR_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_r + (Binary.getVal1(PL(p)).similar(Binary.getVal1(PR(p))) && ((-VLR(p)) & 0x1f) == (VRR(p)&0x1f) ? 13 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x3900; // p.r = 57
			closure_r(p, c);
		}
	}
	if (	// r: INT_OR(INT_USHR(r,INT_CONSTANT),INT_SHL(r,INT_CONSTANT))
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_SHL_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_r + (Binary.getVal1(PL(p)).similar(Binary.getVal1(PR(p))) && ((-VRR(p)) & 0x1f) == (VLR(p)&0x1f) ? 13 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x3A00; // p.r = 58
			closure_r(p, c);
		}
	}
	if (	// r: INT_OR(INT_SHL(r,INT_CONSTANT),INT_USHR(r,INT_CONSTANT))
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_USHR_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_r + (Binary.getVal1(PL(p)).similar(Binary.getVal1(PR(p))) && ((-VLR(p)) & 0x1f) == (VRR(p)&0x1f) && ((VLR(p)&0x1f) == 31) ? 11 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x3B00; // p.r = 59
			closure_r(p, c);
		}
	}
	if (	// r: INT_OR(INT_USHR(r,INT_CONSTANT),INT_SHL(r,INT_CONSTANT))
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_SHL_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_r + (Binary.getVal1(PL(p)).similar(Binary.getVal1(PR(p))) && ((-VRR(p)) & 0x1f) == (VLR(p)&0x1f) && ((VRR(p)&0x1f) == 31) ? 11 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x3C00; // p.r = 60
			closure_r(p, c);
		}
	}
	if (	// r: INT_OR(INT_SHL(r,INT_AND(r,INT_CONSTANT)),INT_USHR(r,INT_AND(INT_NEG(r),INT_CONSTANT)))
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_USHR_opcode && 
		rchild.child2.getOpcode() == INT_AND_opcode && 
		rchild.child2.child1.getOpcode() == INT_NEG_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(lchild.child2.child1).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1.child1).cost_r + (Binary.getVal1(PL(p)).similar(Binary.getVal1(PR(p))) && (VLRR(p) == 31) && (VRRR(p) == 31) && Binary.getVal1(PLR(p)).similar(Unary.getVal(PRRL(p))) ? 23 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x3D00; // p.r = 61
			closure_r(p, c);
		}
	}
	if (	// r: INT_OR(INT_USHR(r,INT_AND(INT_NEG(r),INT_CONSTANT)),INT_SHL(r,INT_AND(r,INT_CONSTANT)))
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child1.getOpcode() == INT_NEG_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_SHL_opcode && 
		rchild.child2.getOpcode() == INT_AND_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(lchild.child2.child1.child1).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_r + (Binary.getVal1(PL(p)).similar(Binary.getVal1(PR(p))) && (VLRR(p) == 31) && (VRRR(p) == 31) && Binary.getVal1(PRR(p)).similar(Unary.getVal(PLRL(p))) ? 23 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x3E00; // p.r = 62
			closure_r(p, c);
		}
	}
	if (	// r: INT_OR(INT_SHL(r,INT_AND(INT_NEG(r),INT_CONSTANT)),INT_USHR(r,INT_AND(r,INT_CONSTANT)))
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child1.getOpcode() == INT_NEG_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_USHR_opcode && 
		rchild.child2.getOpcode() == INT_AND_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(lchild.child2.child1.child1).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_r + (Binary.getVal1(PL(p)).similar(Binary.getVal1(PR(p))) && (VLRR(p) == 31) && (VRRR(p) == 31) && Binary.getVal1(PRR(p)).similar(Unary.getVal(PLRL(p))) ? 23 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x3F00; // p.r = 63
			closure_r(p, c);
		}
	}
	if (	// r: INT_OR(INT_USHR(r,INT_AND(r,INT_CONSTANT)),INT_SHL(r,INT_AND(INT_NEG(r),INT_CONSTANT)))
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == INT_SHL_opcode && 
		rchild.child2.getOpcode() == INT_AND_opcode && 
		rchild.child2.child1.getOpcode() == INT_NEG_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(lchild.child2.child1).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1.child1).cost_r + (Binary.getVal1(PL(p)).similar(Binary.getVal1(PR(p))) && (VLRR(p) == 31) && (VRRR(p) == 31) && Binary.getVal1(PLR(p)).similar(Unary.getVal(PRRL(p))) ? 23 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x4000; // p.r = 64
			closure_r(p, c);
		}
	}
	// szpr: INT_OR(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 13;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x1E00000; // p.szpr = 15
		closure_szpr(p, c);
	}
	// szpr: INT_OR(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 15;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x2000000; // p.szpr = 16
		closure_szpr(p, c);
	}
	// szpr: INT_OR(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 15;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x2200000; // p.szpr = 17
		closure_szpr(p, c);
	}
}

private void label_LONG_OR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_OR(rlv,rlv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_rlv + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4D00; // p.r = 77
		closure_r(p, c);
	}
}

private void label_INT_XOR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// szpr: INT_XOR(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 13;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x2400000; // p.szpr = 18
		closure_szpr(p, c);
	}
	// szpr: INT_XOR(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 15;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x2600000; // p.szpr = 19
		closure_szpr(p, c);
	}
	// szpr: INT_XOR(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 15;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x2800000; // p.szpr = 20
		closure_szpr(p, c);
	}
}

private void label_INT_NOT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: INT_NOT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4100; // p.r = 65
		closure_r(p, c);
	}
}

private void label_LONG_NOT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: LONG_NOT(r)
	c = STATE(lchild).cost_r + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4F00; // p.r = 79
		closure_r(p, c);
	}
}

private void label_LONG_XOR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_XOR(r,rlv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_rlv + 23;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x4E00; // p.r = 78
		closure_r(p, c);
	}
}

private void label_ADDR_2LONG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: ADDR_2LONG(r)
	c = STATE(lchild).cost_r + 33;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5B00; // p.r = 91
		closure_r(p, c);
	}
	// r: ADDR_2LONG(load32)
	c = STATE(lchild).cost_load32 + 38;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5C00; // p.r = 92
		closure_r(p, c);
	}
}

private void label_INT_2LONG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: INT_2LONG(r)
	c = STATE(lchild).cost_r + 33;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5500; // p.r = 85
		closure_r(p, c);
	}
	// r: INT_2LONG(load32)
	c = STATE(lchild).cost_load32 + 38;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5600; // p.r = 86
		closure_r(p, c);
	}
}

private void label_INT_2FLOAT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: INT_2FLOAT(riv)
	c = STATE(lchild).cost_riv + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xBA00; // p.r = 186
		closure_r(p, c);
	}
	// r: INT_2FLOAT(load32)
	c = STATE(lchild).cost_load32 + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xBB00; // p.r = 187
		closure_r(p, c);
	}
}

private void label_INT_2DOUBLE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: INT_2DOUBLE(riv)
	c = STATE(lchild).cost_riv + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xBC00; // p.r = 188
		closure_r(p, c);
	}
	// r: INT_2DOUBLE(load32)
	c = STATE(lchild).cost_load32 + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xBD00; // p.r = 189
		closure_r(p, c);
	}
}

private void label_LONG_2INT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: LONG_2INT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5D00; // p.r = 93
		closure_r(p, c);
	}
	// r: LONG_2INT(load64)
	c = STATE(lchild).cost_load64 + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5E00; // p.r = 94
		closure_r(p, c);
	}
	if (	// r: LONG_2INT(LONG_USHR(r,INT_CONSTANT))
		lchild.getOpcode() == LONG_USHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + (VLR(p) == 32 ? 13 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x5F00; // p.r = 95
			closure_r(p, c);
		}
	}
	if (	// r: LONG_2INT(LONG_SHR(r,INT_CONSTANT))
		lchild.getOpcode() == LONG_SHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_r + (VLR(p) == 32 ? 13 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x6000; // p.r = 96
			closure_r(p, c);
		}
	}
	if (	// r: LONG_2INT(LONG_USHR(load64,INT_CONSTANT))
		lchild.getOpcode() == LONG_USHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load64 + (VLR(p) == 32 ? 15 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x6100; // p.r = 97
			closure_r(p, c);
		}
	}
	if (	// r: LONG_2INT(LONG_SHR(load64,INT_CONSTANT))
		lchild.getOpcode() == LONG_SHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load64 + (VLR(p) == 32 ? 15 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x6200; // p.r = 98
			closure_r(p, c);
		}
	}
	if (	// load32: LONG_2INT(LONG_USHR(load64,INT_CONSTANT))
		lchild.getOpcode() == LONG_USHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load64 + (VLR(p) == 32 ? 0 : INFINITE);
		if (c < p.cost_load32) {
			p.cost_load32 = (char)(c);
			p.word1 = (p.word1 & 0xFFE3FFFF) | 0xC0000; // p.load32 = 3
			closure_load32(p, c);
		}
	}
	if (	// load32: LONG_2INT(LONG_SHR(load64,INT_CONSTANT))
		lchild.getOpcode() == LONG_SHR_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild.child1).cost_load64 + (VLR(p) == 32 ? 0 : INFINITE);
		if (c < p.cost_load32) {
			p.cost_load32 = (char)(c);
			p.word1 = (p.word1 & 0xFFE3FFFF) | 0x100000; // p.load32 = 4
			closure_load32(p, c);
		}
	}
}

private void label_LONG_2FLOAT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: LONG_2FLOAT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB200; // p.r = 178
		closure_r(p, c);
	}
}

private void label_LONG_2DOUBLE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: LONG_2DOUBLE(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB300; // p.r = 179
		closure_r(p, c);
	}
}

private void label_FLOAT_2INT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: FLOAT_2INT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC200; // p.r = 194
		closure_r(p, c);
	}
}

private void label_FLOAT_2LONG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: FLOAT_2LONG(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC300; // p.r = 195
		closure_r(p, c);
	}
}

private void label_FLOAT_2DOUBLE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: FLOAT_2DOUBLE(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xBE00; // p.r = 190
		closure_r(p, c);
	}
	// r: FLOAT_2DOUBLE(float_load)
	c = STATE(lchild).cost_float_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xBF00; // p.r = 191
		closure_r(p, c);
	}
}

private void label_DOUBLE_2INT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: DOUBLE_2INT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC400; // p.r = 196
		closure_r(p, c);
	}
}

private void label_DOUBLE_2LONG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: DOUBLE_2LONG(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC500; // p.r = 197
		closure_r(p, c);
	}
}

private void label_DOUBLE_2FLOAT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: DOUBLE_2FLOAT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC000; // p.r = 192
		closure_r(p, c);
	}
	// r: DOUBLE_2FLOAT(double_load)
	c = STATE(lchild).cost_double_load + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC100; // p.r = 193
		closure_r(p, c);
	}
}

private void label_INT_2BYTE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: INT_2BYTE(r)
	c = STATE(lchild).cost_r + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5000; // p.r = 80
		closure_r(p, c);
	}
	// r: INT_2BYTE(load8_16_32)
	c = STATE(lchild).cost_load8_16_32 + 17;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5100; // p.r = 81
		closure_r(p, c);
	}
	// r: INT_2BYTE(load8_16_32)
	c = STATE(lchild).cost_load8_16_32 + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6C00; // p.r = 108
		closure_r(p, c);
	}
}

private void label_INT_2USHORT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// szpr: INT_2USHORT(r)
	c = STATE(lchild).cost_r + 23;
	if (c < p.cost_szpr) {
		p.cost_szpr = (char)(c);
		p.word0 = (p.word0 & 0xFC1FFFFF) | 0x2A00000; // p.szpr = 21
		closure_szpr(p, c);
	}
	// r: INT_2USHORT(load16_32)
	c = STATE(lchild).cost_load16_32 + 25;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5200; // p.r = 82
		closure_r(p, c);
	}
	// r: INT_2USHORT(load16_32)
	c = STATE(lchild).cost_load16_32 + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7300; // p.r = 115
		closure_r(p, c);
	}
	// r: INT_2USHORT(load16_32)
	c = STATE(lchild).cost_load16_32 + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7400; // p.r = 116
		closure_r(p, c);
	}
}

private void label_INT_2SHORT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: INT_2SHORT(r)
	c = STATE(lchild).cost_r + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5300; // p.r = 83
		closure_r(p, c);
	}
	// r: INT_2SHORT(load16_32)
	c = STATE(lchild).cost_load16_32 + 17;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x5400; // p.r = 84
		closure_r(p, c);
	}
}

private void label_LONG_CMP(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: LONG_CMP(rlv,rlv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_rlv + 10*13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x8F00; // p.r = 143
		closure_r(p, c);
	}
}

private void label_RETURN(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	if (	// stm: RETURN(NULL)
		lchild.getOpcode() == NULL_opcode 
	) {
		c = 13;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x7B; // p.stm = 123
		}
	}
	if (	// stm: RETURN(INT_CONSTANT)
		lchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = 11;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x7C; // p.stm = 124
		}
	}
	// stm: RETURN(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x7D; // p.stm = 125
	}
	if (	// stm: RETURN(LONG_CONSTANT)
		lchild.getOpcode() == LONG_CONSTANT_opcode 
	) {
		c = 11;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x7E; // p.stm = 126
		}
	}
}

private void label_NULL_CHECK(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// stm: NULL_CHECK(riv)
	c = STATE(lchild).cost_riv + 11;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0xB; // p.stm = 11
	}
}

private void label_GOTO(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: GOTO
	if (11 < p.cost_stm) {
		p.cost_stm = (char)(11);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x76; // p.stm = 118
	}
}

private void label_BOOLEAN_NOT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: BOOLEAN_NOT(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xD00; // p.r = 13
		closure_r(p, c);
	}
}

private void label_BOOLEAN_CMP_INT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: BOOLEAN_CMP_INT(r,riv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_riv + 39;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xE00; // p.r = 14
		closure_r(p, c);
	}
	// boolcmp: BOOLEAN_CMP_INT(r,riv)
	c = STATE(lchild).cost_r + STATE(rchild).cost_riv + 13;
	if (c < p.cost_boolcmp) {
		p.cost_boolcmp = (char)(c);
		p.word2 = (p.word2 & 0xFFFFF87F) | 0x80; // p.boolcmp = 1
	}
	if (	// r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + (VR(p) == 0 && CMP_TO_TEST(BooleanCmp.getCond(P(p))) ? 37:INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xF00; // p.r = 15
			closure_r(p, c);
		}
	}
	if (	// boolcmp: BOOLEAN_CMP_INT(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + (VR(p) == 0 && CMP_TO_TEST(BooleanCmp.getCond(P(p))) ? 11:INFINITE);
		if (c < p.cost_boolcmp) {
			p.cost_boolcmp = (char)(c);
			p.word2 = (p.word2 & 0xFFFFF87F) | 0x100; // p.boolcmp = 2
		}
	}
	if (	// r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + (VR(p) == 0 && BooleanCmp.getCond(P(p)).isLESS() ? 11 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1000; // p.r = 16
			closure_r(p, c);
		}
	}
	if (	// r: BOOLEAN_CMP_INT(load32,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load32 + (VR(p) == 0 && BooleanCmp.getCond(P(p)).isLESS() ? 16 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1100; // p.r = 17
			closure_r(p, c);
		}
	}
	if (	// r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + (VR(p) == 0 && BooleanCmp.getCond(P(p)).isGREATER_EQUAL() ? 22 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1200; // p.r = 18
			closure_r(p, c);
		}
	}
	if (	// r: BOOLEAN_CMP_INT(load32,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load32 + (VR(p) == 0 && BooleanCmp.getCond(P(p)).isGREATER_EQUAL() ? 27 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1300; // p.r = 19
			closure_r(p, c);
		}
	}
	if (	// r: BOOLEAN_CMP_INT(cz,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_cz + isZERO(VR(p), 26);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1400; // p.r = 20
			closure_r(p, c);
		}
	}
	if (	// boolcmp: BOOLEAN_CMP_INT(cz,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_cz + isZERO(VR(p), 0);
		if (c < p.cost_boolcmp) {
			p.cost_boolcmp = (char)(c);
			p.word2 = (p.word2 & 0xFFFFF87F) | 0x180; // p.boolcmp = 3
		}
	}
	if (	// r: BOOLEAN_CMP_INT(szp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_szp + (VR(p) == 0 && EQ_NE(BooleanCmp.getCond(P(p)))?26:INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1500; // p.r = 21
			closure_r(p, c);
		}
	}
	if (	// boolcmp: BOOLEAN_CMP_INT(szp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_szp + (VR(p) == 0 && EQ_NE(BooleanCmp.getCond(P(p)))?0:INFINITE);
		if (c < p.cost_boolcmp) {
			p.cost_boolcmp = (char)(c);
			p.word2 = (p.word2 & 0xFFFFF87F) | 0x200; // p.boolcmp = 4
		}
	}
	if (	// r: BOOLEAN_CMP_INT(bittest,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_bittest + ((VR(p) == 0 || VR(p) == 1) && EQ_NE(BooleanCmp.getCond(P(p))) ? 26 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1600; // p.r = 22
			closure_r(p, c);
		}
	}
	if (	// boolcmp: BOOLEAN_CMP_INT(bittest,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_bittest + ((VR(p) == 0 || VR(p) == 1) && EQ_NE(BooleanCmp.getCond(P(p))) ? 0 : INFINITE);
		if (c < p.cost_boolcmp) {
			p.cost_boolcmp = (char)(c);
			p.word2 = (p.word2 & 0xFFFFF87F) | 0x280; // p.boolcmp = 5
		}
	}
	if (	// r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + ((VR(p) == 0 && BooleanCmp.getCond(P(p)).isNOT_EQUAL()) || (VR(p) == 1 && BooleanCmp.getCond(P(p)).isEQUAL()) ? 26 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1700; // p.r = 23
			closure_r(p, c);
		}
	}
	if (	// boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + ((VR(p) == 0 && BooleanCmp.getCond(P(p)).isNOT_EQUAL()) || (VR(p) == 1 && BooleanCmp.getCond(P(p)).isEQUAL()) ? 0 : INFINITE);
		if (c < p.cost_boolcmp) {
			p.cost_boolcmp = (char)(c);
			p.word2 = (p.word2 & 0xFFFFF87F) | 0x300; // p.boolcmp = 6
		}
	}
	if (	// r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + ((VR(p) == 1 && BooleanCmp.getCond(P(p)).isNOT_EQUAL()) || (VR(p) == 0 && BooleanCmp.getCond(P(p)).isEQUAL()) ? 26 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1800; // p.r = 24
			closure_r(p, c);
		}
	}
	if (	// boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + ((VR(p) == 1 && BooleanCmp.getCond(P(p)).isNOT_EQUAL()) || (VR(p) == 0 && BooleanCmp.getCond(P(p)).isEQUAL()) ? 0 : INFINITE);
		if (c < p.cost_boolcmp) {
			p.cost_boolcmp = (char)(c);
			p.word2 = (p.word2 & 0xFFFFF87F) | 0x380; // p.boolcmp = 7
		}
	}
	// r: BOOLEAN_CMP_INT(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 41;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x1900; // p.r = 25
		closure_r(p, c);
	}
	// boolcmp: BOOLEAN_CMP_INT(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 15;
	if (c < p.cost_boolcmp) {
		p.cost_boolcmp = (char)(c);
		p.word2 = (p.word2 & 0xFFFFF87F) | 0x400; // p.boolcmp = 8
	}
	// r: BOOLEAN_CMP_INT(r,load32)
	c = STATE(lchild).cost_r + STATE(rchild).cost_load32 + 41;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x1A00; // p.r = 26
		closure_r(p, c);
	}
	// boolcmp: BOOLEAN_CMP_INT(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 15;
	if (c < p.cost_boolcmp) {
		p.cost_boolcmp = (char)(c);
		p.word2 = (p.word2 & 0xFFFFF87F) | 0x480; // p.boolcmp = 9
	}
}

private void label_BOOLEAN_CMP_LONG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: BOOLEAN_CMP_LONG(rlv,rlv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_rlv + 8*13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x1B00; // p.r = 27
		closure_r(p, c);
	}
	// boolcmp: BOOLEAN_CMP_LONG(rlv,rlv)
	c = STATE(lchild).cost_rlv + STATE(rchild).cost_rlv + 11*13;
	if (c < p.cost_boolcmp) {
		p.cost_boolcmp = (char)(c);
		p.word2 = (p.word2 & 0xFFFFF87F) | 0x500; // p.boolcmp = 10
	}
}

private void label_BYTE_LOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// sload8: BYTE_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_sload8) {
		p.cost_sload8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFF9F) | 0x20; // p.sload8 = 1
		closure_sload8(p, c);
	}
	// r: BYTE_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6700; // p.r = 103
		closure_r(p, c);
	}
}

private void label_UBYTE_LOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// uload8: UBYTE_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_uload8) {
		p.cost_uload8 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFC7F) | 0x80; // p.uload8 = 1
		closure_uload8(p, c);
	}
	// r: UBYTE_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6900; // p.r = 105
		closure_r(p, c);
	}
}

private void label_SHORT_LOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// sload16: SHORT_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_sload16) {
		p.cost_sload16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFFCFFF) | 0x1000; // p.sload16 = 1
		closure_sload16(p, c);
	}
	// r: SHORT_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 20;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6E00; // p.r = 110
		closure_r(p, c);
	}
}

private void label_USHORT_LOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// uload16: USHORT_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_uload16) {
		p.cost_uload16 = (char)(c);
		p.word1 = (p.word1 & 0xFFFF3FFF) | 0x4000; // p.uload16 = 1
		closure_uload16(p, c);
	}
	// r: USHORT_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7000; // p.r = 112
		closure_r(p, c);
	}
}

private void label_INT_LOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// load32: INT_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_load32) {
		p.cost_load32 = (char)(c);
		p.word1 = (p.word1 & 0xFFE3FFFF) | 0x40000; // p.load32 = 1
		closure_load32(p, c);
	}
	// r: INT_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7600; // p.r = 118
		closure_r(p, c);
	}
	// r: INT_LOAD(riv,address1scaledreg)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_address1scaledreg + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7700; // p.r = 119
		closure_r(p, c);
	}
	// r: INT_LOAD(address1scaledreg,riv)
	c = STATE(lchild).cost_address1scaledreg + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7800; // p.r = 120
		closure_r(p, c);
	}
	// r: INT_LOAD(address1scaledreg,address1reg)
	c = STATE(lchild).cost_address1scaledreg + STATE(rchild).cost_address1reg + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7900; // p.r = 121
		closure_r(p, c);
	}
	// r: INT_LOAD(address1reg,address1scaledreg)
	c = STATE(lchild).cost_address1reg + STATE(rchild).cost_address1scaledreg + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7A00; // p.r = 122
		closure_r(p, c);
	}
	if (	// r: INT_LOAD(address,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_address + 15;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x7B00; // p.r = 123
			closure_r(p, c);
		}
	}
}

private void label_LONG_LOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// load64: LONG_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_load64) {
		p.cost_load64 = (char)(c);
		p.word1 = (p.word1 & 0xF1FFFFFF) | 0x2000000; // p.load64 = 1
	}
	// r: LONG_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 30;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7D00; // p.r = 125
		closure_r(p, c);
	}
}

private void label_FLOAT_LOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: FLOAT_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB800; // p.r = 184
		closure_r(p, c);
	}
	// float_load: FLOAT_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_float_load) {
		p.cost_float_load = (char)(c);
		p.word2 = (p.word2 & 0xFFFC7FFF) | 0x8000; // p.float_load = 1
	}
}

private void label_DOUBLE_LOAD(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: DOUBLE_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xB600; // p.r = 182
		closure_r(p, c);
	}
	// double_load: DOUBLE_LOAD(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 0;
	if (c < p.cost_double_load) {
		p.cost_double_load = (char)(c);
		p.word2 = (p.word2 & 0xFFE3FFFF) | 0x40000; // p.double_load = 1
	}
}

private void label_BYTE_STORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: BYTE_STORE(BOOLEAN_NOT(UBYTE_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == BOOLEAN_NOT_opcode && 
		lchild.child1.getOpcode() == UBYTE_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x14; // p.stm = 20
		}
	}
	if (	// stm: BYTE_STORE(boolcmp,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x16; // p.stm = 22
		}
	}
	if (	// stm: BYTE_STORE(INT_2BYTE(r),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_2BYTE_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x3C; // p.stm = 60
		}
	}
	if (	// stm: BYTE_STORE(riv,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x52; // p.stm = 82
		}
	}
	if (	// stm: BYTE_STORE(load8,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_load8 + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 25;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x53; // p.stm = 83
		}
	}
}

private void label_SHORT_STORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: SHORT_STORE(INT_2USHORT(r),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_2USHORT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x3E; // p.stm = 62
		}
	}
	if (	// stm: SHORT_STORE(INT_2SHORT(r),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_2SHORT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x40; // p.stm = 64
		}
	}
	if (	// stm: SHORT_STORE(riv,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x56; // p.stm = 86
		}
	}
	if (	// stm: SHORT_STORE(load16,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_load16 + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 25;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x57; // p.stm = 87
		}
	}
}

private void label_INT_STORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: INT_STORE(INT_ADD(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_ADD_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x18; // p.stm = 24
		}
	}
	if (	// stm: INT_STORE(INT_ADD(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_ADD_opcode && 
		lchild.child2.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLR(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x19; // p.stm = 25
		}
	}
	if (	// stm: INT_STORE(INT_SUB(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SUB_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x1C; // p.stm = 28
		}
	}
	if (	// stm: INT_STORE(INT_SUB(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SUB_opcode && 
		lchild.child2.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLR(p), 27);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x1D; // p.stm = 29
		}
	}
	if (	// stm: INT_STORE(INT_NEG(INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_NEG_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x20; // p.stm = 32
		}
	}
	if (	// stm: INT_STORE(INT_SHL(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ADDRESS_EQUAL(P(p), PLL(p), VLRR(p) == 31 ? 27 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x22; // p.stm = 34
		}
	}
	if (	// stm: INT_STORE(INT_SHL(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SHL_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x23; // p.stm = 35
		}
	}
	if (	// stm: INT_STORE(INT_SHR(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SHR_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ADDRESS_EQUAL(P(p), PLL(p), VLRR(p) == 31 ? 27 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x26; // p.stm = 38
		}
	}
	if (	// stm: INT_STORE(INT_SHR(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_SHR_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x27; // p.stm = 39
		}
	}
	if (	// stm: INT_STORE(INT_USHR(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		lchild.child2.getOpcode() == INT_AND_opcode && 
		lchild.child2.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + (ADDRESS_EQUAL(P(p), PLL(p), VLRR(p) == 31 ? 27 : INFINITE));
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x2A; // p.stm = 42
		}
	}
	if (	// stm: INT_STORE(INT_USHR(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_USHR_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		lchild.child2.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x2B; // p.stm = 43
		}
	}
	if (	// stm: INT_STORE(INT_AND(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_AND_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x2E; // p.stm = 46
		}
	}
	if (	// stm: INT_STORE(INT_AND(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_AND_opcode && 
		lchild.child2.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLR(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x2F; // p.stm = 47
		}
	}
	if (	// stm: INT_STORE(INT_OR(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_OR_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x32; // p.stm = 50
		}
	}
	if (	// stm: INT_STORE(INT_OR(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_OR_opcode && 
		lchild.child2.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLR(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x33; // p.stm = 51
		}
	}
	if (	// stm: INT_STORE(INT_XOR(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_XOR_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x36; // p.stm = 54
		}
	}
	if (	// stm: INT_STORE(INT_XOR(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_XOR_opcode && 
		lchild.child2.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2.child1).cost_riv + STATE(lchild.child2.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLR(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x37; // p.stm = 55
		}
	}
	if (	// stm: INT_STORE(INT_NOT(INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == INT_NOT_opcode && 
		lchild.child1.getOpcode() == INT_LOAD_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1.child1).cost_riv + STATE(lchild.child1.child2).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + ADDRESS_EQUAL(P(p), PLL(p), 17);
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x3A; // p.stm = 58
		}
	}
	if (	// stm: INT_STORE(LONG_2INT(r),OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == LONG_2INT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild.child1).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x42; // p.stm = 66
		}
	}
	if (	// stm: INT_STORE(riv,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x5A; // p.stm = 90
		}
	}
	if (	// stm: INT_STORE(riv,OTHER_OPERAND(riv,address1scaledreg))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_address1scaledreg + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x5B; // p.stm = 91
		}
	}
	if (	// stm: INT_STORE(riv,OTHER_OPERAND(address1scaledreg,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_address1scaledreg + STATE(rchild.child2).cost_riv + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x5C; // p.stm = 92
		}
	}
	if (	// stm: INT_STORE(riv,OTHER_OPERAND(address1scaledreg,address1reg))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_address1scaledreg + STATE(rchild.child2).cost_address1reg + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x5D; // p.stm = 93
		}
	}
	if (	// stm: INT_STORE(riv,OTHER_OPERAND(address1reg,address1scaledreg))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_address1reg + STATE(rchild.child2).cost_address1scaledreg + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x5E; // p.stm = 94
		}
	}
	if (	// stm: INT_STORE(riv,OTHER_OPERAND(address,INT_CONSTANT))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_address + 15;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x5F; // p.stm = 95
		}
	}
}

private void label_LONG_STORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: LONG_STORE(r,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 30;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x61; // p.stm = 97
		}
	}
	if (	// stm: LONG_STORE(LONG_CONSTANT,OTHER_OPERAND(riv,riv))
		lchild.getOpcode() == LONG_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 26;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x62; // p.stm = 98
		}
	}
	if (	// stm: LONG_STORE(load64,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_load64 + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 32;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x8C; // p.stm = 140
		}
	}
}

private void label_FLOAT_STORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: FLOAT_STORE(r,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 17;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x82; // p.stm = 130
		}
	}
}

private void label_DOUBLE_STORE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: DOUBLE_STORE(r,OTHER_OPERAND(riv,riv))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_riv + 17;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x80; // p.stm = 128
		}
	}
}

private void label_PREPARE_INT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: PREPARE_INT(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x7F00; // p.r = 127
		closure_r(p, c);
	}
	// r: PREPARE_INT(r,address1scaledreg)
	c = STATE(lchild).cost_r + STATE(rchild).cost_address1scaledreg + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x8000; // p.r = 128
		closure_r(p, c);
	}
	// r: PREPARE_INT(address1scaledreg,r)
	c = STATE(lchild).cost_address1scaledreg + STATE(rchild).cost_r + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x8100; // p.r = 129
		closure_r(p, c);
	}
	// r: PREPARE_INT(address1scaledreg,address1reg)
	c = STATE(lchild).cost_address1scaledreg + STATE(rchild).cost_address1reg + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x8200; // p.r = 130
		closure_r(p, c);
	}
	// r: PREPARE_INT(address1reg,address1scaledreg)
	c = STATE(lchild).cost_address1reg + STATE(rchild).cost_address1scaledreg + 15;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x8300; // p.r = 131
		closure_r(p, c);
	}
	if (	// r: PREPARE_INT(address,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_address + 15;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8400; // p.r = 132
			closure_r(p, c);
		}
	}
	if (	// r: PREPARE_INT(INT_CONSTANT,address)
		lchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(rchild).cost_address + 15;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8500; // p.r = 133
			closure_r(p, c);
		}
	}
}

private void label_PREPARE_LONG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: PREPARE_LONG(riv,riv)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_riv + 30;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x8600; // p.r = 134
		closure_r(p, c);
	}
}

private void label_ATTEMPT_INT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// r: ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2.child1).cost_riv + STATE(rchild.child2.child2).cost_riv + 67;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8700; // p.r = 135
			closure_r(p, c);
		}
	}
	if (	// r: ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_address1scaledreg + STATE(rchild.child2.child1).cost_riv + STATE(rchild.child2.child2).cost_riv + 67;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8800; // p.r = 136
			closure_r(p, c);
		}
	}
	if (	// r: ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_address1scaledreg + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_riv + STATE(rchild.child2.child2).cost_riv + 67;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8900; // p.r = 137
			closure_r(p, c);
		}
	}
	if (	// r: ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_address1scaledreg + STATE(rchild.child1).cost_address1reg + STATE(rchild.child2.child1).cost_riv + STATE(rchild.child2.child2).cost_riv + 67;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8A00; // p.r = 138
			closure_r(p, c);
		}
	}
	if (	// r: ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_address1reg + STATE(rchild.child1).cost_address1scaledreg + STATE(rchild.child2.child1).cost_riv + STATE(rchild.child2.child2).cost_riv + 67;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8B00; // p.r = 139
			closure_r(p, c);
		}
	}
	if (	// r: ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_address + STATE(rchild.child2.child1).cost_riv + STATE(rchild.child2.child2).cost_riv + 67;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8C00; // p.r = 140
			closure_r(p, c);
		}
	}
	if (	// r: ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv)))
		lchild.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(rchild.child1).cost_address + STATE(rchild.child2.child1).cost_riv + STATE(rchild.child2.child2).cost_riv + 67;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8D00; // p.r = 141
			closure_r(p, c);
		}
	}
}

private void label_ATTEMPT_LONG(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// r: ATTEMPT_LONG(riv,OTHER_OPERAND(riv,OTHER_OPERAND(rlv,rlv)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_riv + STATE(rchild.child2.child1).cost_rlv + STATE(rchild.child2.child2).cost_rlv + 67;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x8E00; // p.r = 142
			closure_r(p, c);
		}
	}
}

private void label_CALL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: CALL(r,any)
	c = STATE(lchild).cost_r + STATE(rchild).cost_any + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9000; // p.r = 144
		closure_r(p, c);
	}
	if (	// r: CALL(BRANCH_TARGET,any)
		lchild.getOpcode() == BRANCH_TARGET_opcode 
	) {
		c = STATE(rchild).cost_any + 13;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x9100; // p.r = 145
			closure_r(p, c);
		}
	}
	if (	// r: CALL(INT_LOAD(riv,riv),any)
		lchild.getOpcode() == INT_LOAD_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild).cost_any + 11;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x9200; // p.r = 146
			closure_r(p, c);
		}
	}
	if (	// r: CALL(INT_CONSTANT,any)
		lchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(rchild).cost_any + 23;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x9300; // p.r = 147
			closure_r(p, c);
		}
	}
}

private void label_SYSCALL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// r: SYSCALL(r,any)
	c = STATE(lchild).cost_r + STATE(rchild).cost_any + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9400; // p.r = 148
		closure_r(p, c);
	}
	if (	// r: SYSCALL(INT_LOAD(riv,riv),any)
		lchild.getOpcode() == INT_LOAD_opcode 
	) {
		c = STATE(lchild.child1).cost_riv + STATE(lchild.child2).cost_riv + STATE(rchild).cost_any + 11;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x9500; // p.r = 149
			closure_r(p, c);
		}
	}
	if (	// r: SYSCALL(INT_CONSTANT,any)
		lchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(rchild).cost_any + 23;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x9600; // p.r = 150
			closure_r(p, c);
		}
	}
}

private void label_YIELDPOINT_PROLOGUE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: YIELDPOINT_PROLOGUE
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x5; // p.stm = 5
	}
}

private void label_YIELDPOINT_EPILOGUE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: YIELDPOINT_EPILOGUE
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x6; // p.stm = 6
	}
}

private void label_YIELDPOINT_BACKEDGE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: YIELDPOINT_BACKEDGE
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x7; // p.stm = 7
	}
}

private void label_YIELDPOINT_OSR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// stm: YIELDPOINT_OSR(any,any)
	c = STATE(lchild).cost_any + STATE(rchild).cost_any + 10;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x7F; // p.stm = 127
	}
}

private void label_IR_PROLOGUE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: IR_PROLOGUE
	if (11 < p.cost_stm) {
		p.cost_stm = (char)(11);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0xC; // p.stm = 12
	}
}

private void label_RESOLVE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: RESOLVE
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x9; // p.stm = 9
	}
}

private void label_GET_TIME_BASE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// r: GET_TIME_BASE
	if (15 < p.cost_r) {
		p.cost_r = (char)(15);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x9700; // p.r = 151
		closure_r(p, 15);
	}
}

private void label_TRAP_IF(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// stm: TRAP_IF(r,INT_CONSTANT)
		rchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + 10;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0xF; // p.stm = 15
		}
	}
	if (	// stm: TRAP_IF(r,LONG_CONSTANT)
		rchild.getOpcode() == LONG_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + 10;
		if (c < p.cost_stm) {
			p.cost_stm = (char)(c);
			p.word0 = (p.word0 & 0xFFFFFF00) | 0x10; // p.stm = 16
		}
	}
	// stm: TRAP_IF(r,r)
	c = STATE(lchild).cost_r + STATE(rchild).cost_r + 10;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x11; // p.stm = 17
	}
	// stm: TRAP_IF(load32,riv)
	c = STATE(lchild).cost_load32 + STATE(rchild).cost_riv + 15;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x12; // p.stm = 18
	}
	// stm: TRAP_IF(riv,load32)
	c = STATE(lchild).cost_riv + STATE(rchild).cost_load32 + 15;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x13; // p.stm = 19
	}
}

private void label_TRAP(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: TRAP
	if (10 < p.cost_stm) {
		p.cost_stm = (char)(10);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0xE; // p.stm = 14
	}
}

private void label_FLOAT_AS_INT_BITS(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: FLOAT_AS_INT_BITS(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC600; // p.r = 198
		closure_r(p, c);
	}
	// load32: FLOAT_AS_INT_BITS(float_load)
	c = STATE(lchild).cost_float_load + 0;
	if (c < p.cost_load32) {
		p.cost_load32 = (char)(c);
		p.word1 = (p.word1 & 0xFFE3FFFF) | 0x180000; // p.load32 = 6
		closure_load32(p, c);
	}
}

private void label_INT_BITS_AS_FLOAT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: INT_BITS_AS_FLOAT(riv)
	c = STATE(lchild).cost_riv + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC800; // p.r = 200
		closure_r(p, c);
	}
	// float_load: INT_BITS_AS_FLOAT(load32)
	c = STATE(lchild).cost_load32 + 0;
	if (c < p.cost_float_load) {
		p.cost_float_load = (char)(c);
		p.word2 = (p.word2 & 0xFFFC7FFF) | 0x18000; // p.float_load = 3
	}
}

private void label_DOUBLE_AS_LONG_BITS(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: DOUBLE_AS_LONG_BITS(r)
	c = STATE(lchild).cost_r + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC700; // p.r = 199
		closure_r(p, c);
	}
	// load64: DOUBLE_AS_LONG_BITS(double_load)
	c = STATE(lchild).cost_double_load + 0;
	if (c < p.cost_load64) {
		p.cost_load64 = (char)(c);
		p.word1 = (p.word1 & 0xF1FFFFFF) | 0x8000000; // p.load64 = 4
	}
}

private void label_LONG_BITS_AS_DOUBLE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// r: LONG_BITS_AS_DOUBLE(rlv)
	c = STATE(lchild).cost_rlv + 13;
	if (c < p.cost_r) {
		p.cost_r = (char)(c);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0xC900; // p.r = 201
		closure_r(p, c);
	}
	// double_load: LONG_BITS_AS_DOUBLE(load64)
	c = STATE(lchild).cost_load64 + 0;
	if (c < p.cost_double_load) {
		p.cost_double_load = (char)(c);
		p.word2 = (p.word2 & 0xFFE3FFFF) | 0xC0000; // p.double_load = 3
	}
}

private void label_LOWTABLESWITCH(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// stm: LOWTABLESWITCH(r)
	c = STATE(lchild).cost_r + 10;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x8; // p.stm = 8
	}
}

private void label_ADDRESS_CONSTANT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// any: ADDRESS_CONSTANT
	if (0 < p.cost_any) {
		p.cost_any = (char)(0);
		p.word1 = (p.word1 & 0xFFFFFFE3) | 0xC; // p.any = 3
	}
}

private void label_INT_CONSTANT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// riv: INT_CONSTANT
	if (0 < p.cost_riv) {
		p.cost_riv = (char)(0);
		p.word0 = (p.word0 & 0x9FFFFFFF) | 0x40000000; // p.riv = 2
		closure_riv(p, 0);
	}
}

private void label_LONG_CONSTANT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// rlv: LONG_CONSTANT
	if (0 < p.cost_rlv) {
		p.cost_rlv = (char)(0);
		p.word1 = (p.word1 & 0xFFFFFFFC) | 0x2; // p.rlv = 2
	}
	// any: LONG_CONSTANT
	if (0 < p.cost_any) {
		p.cost_any = (char)(0);
		p.word1 = (p.word1 & 0xFFFFFFE3) | 0x10; // p.any = 4
	}
}

private void label_REGISTER(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// r: REGISTER
	if (0 < p.cost_r) {
		p.cost_r = (char)(0);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x100; // p.r = 1
		closure_r(p, 0);
	}
}

private void label_OTHER_OPERAND(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	// any: OTHER_OPERAND(any,any)
	c = STATE(lchild).cost_any + STATE(rchild).cost_any + 0;
	if (c < p.cost_any) {
		p.cost_any = (char)(c);
		p.word1 = (p.word1 & 0xFFFFFFE3) | 0x14; // p.any = 5
	}
}

private void label_NULL(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// any: NULL
	if (0 < p.cost_any) {
		p.cost_any = (char)(0);
		p.word1 = (p.word1 & 0xFFFFFFE3) | 0x4; // p.any = 1
	}
}

private void label_BRANCH_TARGET(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
}

private void label_MATERIALIZE_FP_CONSTANT(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	if (	// r: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
		lchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = 15;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xCA00; // p.r = 202
			closure_r(p, c);
		}
	}
	if (	// float_load: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
		lchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = (Binary.getResult(P(p)).isFloat() ? 0 : INFINITE);
		if (c < p.cost_float_load) {
			p.cost_float_load = (char)(c);
			p.word2 = (p.word2 & 0xFFFC7FFF) | 0x20000; // p.float_load = 4
		}
	}
	if (	// double_load: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
		lchild.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = (Binary.getResult(P(p)).isDouble() ? 0 : INFINITE);
		if (c < p.cost_double_load) {
			p.cost_double_load = (char)(c);
			p.word2 = (p.word2 & 0xFFE3FFFF) | 0x100000; // p.double_load = 4
		}
	}
}

private void label_GET_CURRENT_PROCESSOR(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// r: GET_CURRENT_PROCESSOR
	if (15 < p.cost_r) {
		p.cost_r = (char)(15);
		p.word0 = (p.word0 & 0xFFFF00FF) | 0x6600; // p.r = 102
		closure_r(p, 15);
	}
}

private void label_CLEAR_FLOATING_POINT_STATE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: CLEAR_FLOATING_POINT_STATE
	if (0 < p.cost_stm) {
		p.cost_stm = (char)(0);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x84; // p.stm = 132
	}
}

private void label_PREFETCH(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild;
	lchild = p.child1;
	label(lchild);
	int c;
	// stm: PREFETCH(r)
	c = STATE(lchild).cost_r + 11;
	if (c < p.cost_stm) {
		p.cost_stm = (char)(c);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x77; // p.stm = 119
	}
}

private void label_PAUSE(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	// stm: PAUSE
	if (11 < p.cost_stm) {
		p.cost_stm = (char)(11);
		p.word0 = (p.word0 & 0xFFFFFF00) | 0x7A; // p.stm = 122
	}
}

private void label_CMP_CMOV(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// r: CMP_CMOV(r,OTHER_OPERAND(riv,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_any + (13 + 30);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1C00; // p.r = 28
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child2).cost_any + (VRL(p) == 0 && CMP_TO_TEST(CondMove.getCond(P(p))) ? (11 + 30):INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1D00; // p.r = 29
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + ((VRL(p) == 0 && CondMove.getCond(P(p)).isLESS() && VRRL(p) == -1 && VRRR(p) == 0) || (VRL(p) == 0 && CondMove.getCond(P(p)).isGREATER_EQUAL() && VRRL(p) == 0 && VRRR(p) == -1) ? 13 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1E00; // p.r = 30
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(load32,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load32 + ((VRL(p) == 0 && CondMove.getCond(P(p)).isLESS() && VRRL(p) == -1 && VRRR(p) == 0) || (VRL(p) == 0 && CondMove.getCond(P(p)).isGREATER_EQUAL() && VRRL(p) == 0 && VRRR(p) == -1) ? 18 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x1F00; // p.r = 31
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_r + ((VRL(p) == 0 && CondMove.getCond(P(p)).isLESS() && VRRL(p) == 0 && VRRR(p) == -1) || (VRL(p) == 0 && CondMove.getCond(P(p)).isGREATER_EQUAL() && VRRL(p) == -1 && VRRR(p) == 0) ? 26 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2000; // p.r = 32
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(load32,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.child2.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load32 + ((VRL(p) == 0 && CondMove.getCond(P(p)).isLESS() && VRRL(p) == 0 && VRRR(p) == -1) || (VRL(p) == 0 && CondMove.getCond(P(p)).isGREATER_EQUAL() && VRRL(p) == -1 && VRRR(p) == 0) ? 31 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2100; // p.r = 33
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(load8,OTHER_OPERAND(INT_CONSTANT,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_load8 + STATE(rchild.child2).cost_any + FITS(CondMove.getVal2(P(p)), 8, (15 + 30));
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2200; // p.r = 34
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(uload8,OTHER_OPERAND(riv,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_uload8 + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_any + (15 + 30);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2300; // p.r = 35
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(riv,OTHER_OPERAND(uload8,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_uload8 + STATE(rchild.child2).cost_any + (15 + 30);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2400; // p.r = 36
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(sload16,OTHER_OPERAND(INT_CONSTANT,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_sload16 + STATE(rchild.child2).cost_any + FITS(CondMove.getVal2(P(p)), 8, (15 + 30));
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2500; // p.r = 37
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(load32,OTHER_OPERAND(riv,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_load32 + STATE(rchild.child1).cost_riv + STATE(rchild.child2).cost_any + (15 + 30);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2600; // p.r = 38
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(riv,OTHER_OPERAND(load32,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_riv + STATE(rchild.child1).cost_load32 + STATE(rchild.child2).cost_any + (15 + 30);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2700; // p.r = 39
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(boolcmp,OTHER_OPERAND(INT_CONSTANT,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + STATE(rchild.child2).cost_any + ((VRL(p) == 0 && CondMove.getCond(P(p)).isNOT_EQUAL()) || (VRL(p) == 1 && CondMove.getCond(P(p)).isEQUAL()) ? 30 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2800; // p.r = 40
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(boolcmp,OTHER_OPERAND(INT_CONSTANT,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_boolcmp + STATE(rchild.child2).cost_any + ((VRL(p) == 1 && CondMove.getCond(P(p)).isNOT_EQUAL()) || (VRL(p) == 0 && CondMove.getCond(P(p)).isEQUAL()) ? 30 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2900; // p.r = 41
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(bittest,OTHER_OPERAND(INT_CONSTANT,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_bittest + STATE(rchild.child2).cost_any + ((VRL(p) == 0 || VRL(p) == 1) && EQ_NE(CondMove.getCond(P(p))) ? 30 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2A00; // p.r = 42
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(cz,OTHER_OPERAND(INT_CONSTANT,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_cz + STATE(rchild.child2).cost_any + isZERO(VRL(p), 30);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2B00; // p.r = 43
			closure_r(p, c);
		}
	}
	if (	// r: CMP_CMOV(szp,OTHER_OPERAND(INT_CONSTANT,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == INT_CONSTANT_opcode 
	) {
		c = STATE(lchild).cost_szp + STATE(rchild.child2).cost_any + isZERO(VRL(p), 30);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2C00; // p.r = 44
			closure_r(p, c);
		}
	}
}

private void label_FCMP_CMOV(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// r: FCMP_CMOV(r,OTHER_OPERAND(r,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2).cost_any + 13*2;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xCB00; // p.r = 203
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_CMOV(r,OTHER_OPERAND(float_load,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_float_load + STATE(rchild.child2).cost_any + 13+15;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xCC00; // p.r = 204
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_CMOV(r,OTHER_OPERAND(double_load,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_double_load + STATE(rchild.child2).cost_any + 13+15;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xCD00; // p.r = 205
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_CMOV(float_load,OTHER_OPERAND(r,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_float_load + STATE(rchild.child1).cost_r + STATE(rchild.child2).cost_any + 13+15;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xCE00; // p.r = 206
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_CMOV(double_load,OTHER_OPERAND(r,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_double_load + STATE(rchild.child1).cost_r + STATE(rchild.child2).cost_any + 13+15;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xCF00; // p.r = 207
			closure_r(p, c);
		}
	}
}

private void label_LCMP_CMOV(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// r: LCMP_CMOV(r,OTHER_OPERAND(rlv,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_rlv + STATE(rchild.child2).cost_any + (8*13 + 30);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0x2D00; // p.r = 45
			closure_r(p, c);
		}
	}
}

private void label_FCMP_FCMOV(BURS_TreeNode p) {
	p.word0 = 0;
	p.initCost();
	BURS_TreeNode lchild, rchild;
	lchild = p.child1;
	rchild = p.child2;
	label(lchild);
	label(rchild);
	int c;
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(r,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2).cost_any + 13*4;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD000; // p.r = 208
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(r,float_load)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_r + STATE(rchild.child2.child2).cost_float_load + 15+13*3;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD100; // p.r = 209
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(r,double_load)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_r + STATE(rchild.child2.child2).cost_double_load + 15+13*3;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD200; // p.r = 210
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(float_load,r)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_float_load + STATE(rchild.child2.child2).cost_r + 15+13*3;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD300; // p.r = 211
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(double_load,r)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_double_load + STATE(rchild.child2.child2).cost_r + 15+13*3;
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD400; // p.r = 212
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(float_load,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_float_load + STATE(rchild.child2).cost_any + (SSE2_CMP_OP(CondMove.getCond(P(p)), true) != null ? 15+13*3 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD500; // p.r = 213
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(double_load,any))
		rchild.getOpcode() == OTHER_OPERAND_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child1).cost_double_load + STATE(rchild.child2).cost_any + (SSE2_CMP_OP(CondMove.getCond(P(p)), false) != null ? 15+13*3 : INFINITE);
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD600; // p.r = 214
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,FLOAT_NEG(r))))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == MATERIALIZE_FP_CONSTANT_opcode && 
		rchild.child1.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child2.getOpcode() == FLOAT_NEG_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child2.child1).cost_r + STATE(rchild.child2.child2.child1).cost_r + (IS_MATERIALIZE_ZERO(PRL(p)) && SSE2_IS_GT_OR_GE(CondMove.getCond(P(p))) && SIMILAR_REGISTERS(CondMove.getVal1(P(p)), CondMove.getTrueValue(P(p)), Unary.getVal(PRRR(p))) ? 11 : INFINITE );
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD700; // p.r = 215
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(FLOAT_NEG(r),r)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == MATERIALIZE_FP_CONSTANT_opcode && 
		rchild.child1.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child1.getOpcode() == FLOAT_NEG_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child2.child1.child1).cost_r + STATE(rchild.child2.child2).cost_r + (IS_MATERIALIZE_ZERO(PRL(p)) && SSE2_IS_LT_OR_LE(CondMove.getCond(P(p))) && SIMILAR_REGISTERS(CondMove.getVal1(P(p)), CondMove.getFalseValue(P(p)), Unary.getVal(PRRL(p))) ? 11 : INFINITE );
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD800; // p.r = 216
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(FLOAT_NEG(r),r)))
		lchild.getOpcode() == MATERIALIZE_FP_CONSTANT_opcode && 
		lchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child1.getOpcode() == FLOAT_NEG_opcode 
	) {
		c = STATE(rchild.child1).cost_r + STATE(rchild.child2.child1.child1).cost_r + STATE(rchild.child2.child2).cost_r + (IS_MATERIALIZE_ZERO(PRL(p)) && SSE2_IS_GT_OR_GE(CondMove.getCond(P(p))) && SIMILAR_REGISTERS(CondMove.getVal2(P(p)), CondMove.getFalseValue(P(p)), Unary.getVal(PRRL(p))) ? 11 : INFINITE );
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xD900; // p.r = 217
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(r,FLOAT_NEG(r))))
		lchild.getOpcode() == MATERIALIZE_FP_CONSTANT_opcode && 
		lchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child2.getOpcode() == FLOAT_NEG_opcode 
	) {
		c = STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_r + STATE(rchild.child2.child2.child1).cost_r + (IS_MATERIALIZE_ZERO(PRL(p)) && SSE2_IS_LT_OR_LE(CondMove.getCond(P(p))) && SIMILAR_REGISTERS(CondMove.getVal2(P(p)), CondMove.getTrueValue(P(p)), Unary.getVal(PRRR(p))) ? 11 : INFINITE );
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xDA00; // p.r = 218
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,DOUBLE_NEG(r))))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == MATERIALIZE_FP_CONSTANT_opcode && 
		rchild.child1.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child2.getOpcode() == DOUBLE_NEG_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child2.child1).cost_r + STATE(rchild.child2.child2.child1).cost_r + (IS_MATERIALIZE_ZERO(PRL(p)) && SSE2_IS_GT_OR_GE(CondMove.getCond(P(p))) && SIMILAR_REGISTERS(CondMove.getVal1(P(p)), CondMove.getTrueValue(P(p)), Unary.getVal(PRRR(p))) ? 11 : INFINITE );
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xDB00; // p.r = 219
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(DOUBLE_NEG(r),r)))
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child1.getOpcode() == MATERIALIZE_FP_CONSTANT_opcode && 
		rchild.child1.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child1.getOpcode() == DOUBLE_NEG_opcode 
	) {
		c = STATE(lchild).cost_r + STATE(rchild.child2.child1.child1).cost_r + STATE(rchild.child2.child2).cost_r + (IS_MATERIALIZE_ZERO(PRL(p)) && SSE2_IS_LT_OR_LE(CondMove.getCond(P(p))) && SIMILAR_REGISTERS(CondMove.getVal1(P(p)), CondMove.getFalseValue(P(p)), Unary.getVal(PRRL(p))) ? 11 : INFINITE );
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xDC00; // p.r = 220
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(DOUBLE_NEG(r),r)))
		lchild.getOpcode() == MATERIALIZE_FP_CONSTANT_opcode && 
		lchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child1.getOpcode() == DOUBLE_NEG_opcode 
	) {
		c = STATE(rchild.child1).cost_r + STATE(rchild.child2.child1.child1).cost_r + STATE(rchild.child2.child2).cost_r + (IS_MATERIALIZE_ZERO(PRL(p)) && SSE2_IS_GT_OR_GE(CondMove.getCond(P(p))) && SIMILAR_REGISTERS(CondMove.getVal2(P(p)), CondMove.getFalseValue(P(p)), Unary.getVal(PRRL(p))) ? 11 : INFINITE );
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xDD00; // p.r = 221
			closure_r(p, c);
		}
	}
	if (	// r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(r,DOUBLE_NEG(r))))
		lchild.getOpcode() == MATERIALIZE_FP_CONSTANT_opcode && 
		lchild.child1.getOpcode() == INT_CONSTANT_opcode && 
		rchild.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.getOpcode() == OTHER_OPERAND_opcode && 
		rchild.child2.child2.getOpcode() == DOUBLE_NEG_opcode 
	) {
		c = STATE(rchild.child1).cost_r + STATE(rchild.child2.child1).cost_r + STATE(rchild.child2.child2.child1).cost_r + (IS_MATERIALIZE_ZERO(PRL(p)) && SSE2_IS_LT_OR_LE(CondMove.getCond(P(p))) && SIMILAR_REGISTERS(CondMove.getVal2(P(p)), CondMove.getTrueValue(P(p)), Unary.getVal(PRRR(p))) ? 11 : INFINITE );
		if (c < p.cost_r) {
			p.cost_r = (char)(c);
			p.word0 = (p.word0 & 0xFFFF00FF) | 0xDE00; // p.r = 222
			closure_r(p, c);
		}
	}
}

public void label(BURS_TreeNode p) {
	p.initCost();
	switch (p.getOpcode()) {
	case GET_CAUGHT_EXCEPTION_opcode: label_GET_CAUGHT_EXCEPTION(p); break;
	case SET_CAUGHT_EXCEPTION_opcode: label_SET_CAUGHT_EXCEPTION(p); break;
	case IG_PATCH_POINT_opcode: label_IG_PATCH_POINT(p); break;
	case INT_ALOAD_opcode: label_INT_ALOAD(p); break;
	case LONG_ALOAD_opcode: label_LONG_ALOAD(p); break;
	case FLOAT_ALOAD_opcode: label_FLOAT_ALOAD(p); break;
	case DOUBLE_ALOAD_opcode: label_DOUBLE_ALOAD(p); break;
	case UBYTE_ALOAD_opcode: label_UBYTE_ALOAD(p); break;
	case BYTE_ALOAD_opcode: label_BYTE_ALOAD(p); break;
	case USHORT_ALOAD_opcode: label_USHORT_ALOAD(p); break;
	case SHORT_ALOAD_opcode: label_SHORT_ALOAD(p); break;
	case INT_ASTORE_opcode: label_INT_ASTORE(p); break;
	case LONG_ASTORE_opcode: label_LONG_ASTORE(p); break;
	case FLOAT_ASTORE_opcode: label_FLOAT_ASTORE(p); break;
	case DOUBLE_ASTORE_opcode: label_DOUBLE_ASTORE(p); break;
	case BYTE_ASTORE_opcode: label_BYTE_ASTORE(p); break;
	case SHORT_ASTORE_opcode: label_SHORT_ASTORE(p); break;
	case INT_IFCMP_opcode: label_INT_IFCMP(p); break;
	case INT_IFCMP2_opcode: label_INT_IFCMP2(p); break;
	case LONG_IFCMP_opcode: label_LONG_IFCMP(p); break;
	case FLOAT_IFCMP_opcode: label_FLOAT_IFCMP(p); break;
	case DOUBLE_IFCMP_opcode: label_DOUBLE_IFCMP(p); break;
	case UNINT_BEGIN_opcode: label_UNINT_BEGIN(p); break;
	case UNINT_END_opcode: label_UNINT_END(p); break;
	case READ_CEILING_opcode: label_READ_CEILING(p); break;
	case WRITE_FLOOR_opcode: label_WRITE_FLOOR(p); break;
	case NOP_opcode: label_NOP(p); break;
	case INT_MOVE_opcode: label_INT_MOVE(p); break;
	case LONG_MOVE_opcode: label_LONG_MOVE(p); break;
	case FLOAT_MOVE_opcode: label_FLOAT_MOVE(p); break;
	case DOUBLE_MOVE_opcode: label_DOUBLE_MOVE(p); break;
	case GUARD_MOVE_opcode: label_GUARD_MOVE(p); break;
	case GUARD_COMBINE_opcode: label_GUARD_COMBINE(p); break;
	case INT_ADD_opcode: label_INT_ADD(p); break;
	case LONG_ADD_opcode: label_LONG_ADD(p); break;
	case FLOAT_ADD_opcode: label_FLOAT_ADD(p); break;
	case DOUBLE_ADD_opcode: label_DOUBLE_ADD(p); break;
	case INT_SUB_opcode: label_INT_SUB(p); break;
	case LONG_SUB_opcode: label_LONG_SUB(p); break;
	case FLOAT_SUB_opcode: label_FLOAT_SUB(p); break;
	case DOUBLE_SUB_opcode: label_DOUBLE_SUB(p); break;
	case INT_MUL_opcode: label_INT_MUL(p); break;
	case LONG_MUL_opcode: label_LONG_MUL(p); break;
	case FLOAT_MUL_opcode: label_FLOAT_MUL(p); break;
	case DOUBLE_MUL_opcode: label_DOUBLE_MUL(p); break;
	case INT_DIV_opcode: label_INT_DIV(p); break;
	case FLOAT_DIV_opcode: label_FLOAT_DIV(p); break;
	case DOUBLE_DIV_opcode: label_DOUBLE_DIV(p); break;
	case INT_REM_opcode: label_INT_REM(p); break;
	case FLOAT_REM_opcode: label_FLOAT_REM(p); break;
	case DOUBLE_REM_opcode: label_DOUBLE_REM(p); break;
	case INT_NEG_opcode: label_INT_NEG(p); break;
	case LONG_NEG_opcode: label_LONG_NEG(p); break;
	case FLOAT_NEG_opcode: label_FLOAT_NEG(p); break;
	case DOUBLE_NEG_opcode: label_DOUBLE_NEG(p); break;
	case FLOAT_SQRT_opcode: label_FLOAT_SQRT(p); break;
	case DOUBLE_SQRT_opcode: label_DOUBLE_SQRT(p); break;
	case INT_SHL_opcode: label_INT_SHL(p); break;
	case LONG_SHL_opcode: label_LONG_SHL(p); break;
	case INT_SHR_opcode: label_INT_SHR(p); break;
	case LONG_SHR_opcode: label_LONG_SHR(p); break;
	case INT_USHR_opcode: label_INT_USHR(p); break;
	case LONG_USHR_opcode: label_LONG_USHR(p); break;
	case INT_AND_opcode: label_INT_AND(p); break;
	case LONG_AND_opcode: label_LONG_AND(p); break;
	case INT_OR_opcode: label_INT_OR(p); break;
	case LONG_OR_opcode: label_LONG_OR(p); break;
	case INT_XOR_opcode: label_INT_XOR(p); break;
	case INT_NOT_opcode: label_INT_NOT(p); break;
	case LONG_NOT_opcode: label_LONG_NOT(p); break;
	case LONG_XOR_opcode: label_LONG_XOR(p); break;
	case ADDR_2LONG_opcode: label_ADDR_2LONG(p); break;
	case INT_2LONG_opcode: label_INT_2LONG(p); break;
	case INT_2FLOAT_opcode: label_INT_2FLOAT(p); break;
	case INT_2DOUBLE_opcode: label_INT_2DOUBLE(p); break;
	case LONG_2INT_opcode: label_LONG_2INT(p); break;
	case LONG_2FLOAT_opcode: label_LONG_2FLOAT(p); break;
	case LONG_2DOUBLE_opcode: label_LONG_2DOUBLE(p); break;
	case FLOAT_2INT_opcode: label_FLOAT_2INT(p); break;
	case FLOAT_2LONG_opcode: label_FLOAT_2LONG(p); break;
	case FLOAT_2DOUBLE_opcode: label_FLOAT_2DOUBLE(p); break;
	case DOUBLE_2INT_opcode: label_DOUBLE_2INT(p); break;
	case DOUBLE_2LONG_opcode: label_DOUBLE_2LONG(p); break;
	case DOUBLE_2FLOAT_opcode: label_DOUBLE_2FLOAT(p); break;
	case INT_2BYTE_opcode: label_INT_2BYTE(p); break;
	case INT_2USHORT_opcode: label_INT_2USHORT(p); break;
	case INT_2SHORT_opcode: label_INT_2SHORT(p); break;
	case LONG_CMP_opcode: label_LONG_CMP(p); break;
	case RETURN_opcode: label_RETURN(p); break;
	case NULL_CHECK_opcode: label_NULL_CHECK(p); break;
	case GOTO_opcode: label_GOTO(p); break;
	case BOOLEAN_NOT_opcode: label_BOOLEAN_NOT(p); break;
	case BOOLEAN_CMP_INT_opcode: label_BOOLEAN_CMP_INT(p); break;
	case BOOLEAN_CMP_LONG_opcode: label_BOOLEAN_CMP_LONG(p); break;
	case BYTE_LOAD_opcode: label_BYTE_LOAD(p); break;
	case UBYTE_LOAD_opcode: label_UBYTE_LOAD(p); break;
	case SHORT_LOAD_opcode: label_SHORT_LOAD(p); break;
	case USHORT_LOAD_opcode: label_USHORT_LOAD(p); break;
	case INT_LOAD_opcode: label_INT_LOAD(p); break;
	case LONG_LOAD_opcode: label_LONG_LOAD(p); break;
	case FLOAT_LOAD_opcode: label_FLOAT_LOAD(p); break;
	case DOUBLE_LOAD_opcode: label_DOUBLE_LOAD(p); break;
	case BYTE_STORE_opcode: label_BYTE_STORE(p); break;
	case SHORT_STORE_opcode: label_SHORT_STORE(p); break;
	case INT_STORE_opcode: label_INT_STORE(p); break;
	case LONG_STORE_opcode: label_LONG_STORE(p); break;
	case FLOAT_STORE_opcode: label_FLOAT_STORE(p); break;
	case DOUBLE_STORE_opcode: label_DOUBLE_STORE(p); break;
	case PREPARE_INT_opcode: label_PREPARE_INT(p); break;
	case PREPARE_LONG_opcode: label_PREPARE_LONG(p); break;
	case ATTEMPT_INT_opcode: label_ATTEMPT_INT(p); break;
	case ATTEMPT_LONG_opcode: label_ATTEMPT_LONG(p); break;
	case CALL_opcode: label_CALL(p); break;
	case SYSCALL_opcode: label_SYSCALL(p); break;
	case YIELDPOINT_PROLOGUE_opcode: label_YIELDPOINT_PROLOGUE(p); break;
	case YIELDPOINT_EPILOGUE_opcode: label_YIELDPOINT_EPILOGUE(p); break;
	case YIELDPOINT_BACKEDGE_opcode: label_YIELDPOINT_BACKEDGE(p); break;
	case YIELDPOINT_OSR_opcode: label_YIELDPOINT_OSR(p); break;
	case IR_PROLOGUE_opcode: label_IR_PROLOGUE(p); break;
	case RESOLVE_opcode: label_RESOLVE(p); break;
	case GET_TIME_BASE_opcode: label_GET_TIME_BASE(p); break;
	case TRAP_IF_opcode: label_TRAP_IF(p); break;
	case TRAP_opcode: label_TRAP(p); break;
	case FLOAT_AS_INT_BITS_opcode: label_FLOAT_AS_INT_BITS(p); break;
	case INT_BITS_AS_FLOAT_opcode: label_INT_BITS_AS_FLOAT(p); break;
	case DOUBLE_AS_LONG_BITS_opcode: label_DOUBLE_AS_LONG_BITS(p); break;
	case LONG_BITS_AS_DOUBLE_opcode: label_LONG_BITS_AS_DOUBLE(p); break;
	case LOWTABLESWITCH_opcode: label_LOWTABLESWITCH(p); break;
	case ADDRESS_CONSTANT_opcode: label_ADDRESS_CONSTANT(p); break;
	case INT_CONSTANT_opcode: label_INT_CONSTANT(p); break;
	case LONG_CONSTANT_opcode: label_LONG_CONSTANT(p); break;
	case REGISTER_opcode: label_REGISTER(p); break;
	case OTHER_OPERAND_opcode: label_OTHER_OPERAND(p); break;
	case NULL_opcode: label_NULL(p); break;
	case BRANCH_TARGET_opcode: label_BRANCH_TARGET(p); break;
	case MATERIALIZE_FP_CONSTANT_opcode: label_MATERIALIZE_FP_CONSTANT(p); break;
	case GET_CURRENT_PROCESSOR_opcode: label_GET_CURRENT_PROCESSOR(p); break;
	case CLEAR_FLOATING_POINT_STATE_opcode: label_CLEAR_FLOATING_POINT_STATE(p); break;
	case PREFETCH_opcode: label_PREFETCH(p); break;
	case PAUSE_opcode: label_PAUSE(p); break;
	case CMP_CMOV_opcode: label_CMP_CMOV(p); break;
	case FCMP_CMOV_opcode: label_FCMP_CMOV(p); break;
	case LCMP_CMOV_opcode: label_LCMP_CMOV(p); break;
	case FCMP_FCMOV_opcode: label_FCMP_FCMOV(p); break;
	default:
		throw new OptimizingCompilerException("BURS","terminal not in grammar:",OperatorNames.operatorName[p.getOpcode()]);	}
}

static BURS_TreeNode kids(BURS_TreeNode p, int eruleno, int kidnumber)  { 
	if (BURS.DEBUG) {
	switch (eruleno) {
	case 37: // address: address1scaledreg
	case 36: // address1scaledreg: address1reg
	case 33: // load8_16_32: load8
	case 32: // load8_16_32: load16_32
	case 31: // load16_32: load32
	case 30: // load16_32: load16
	case 27: // load16: uload16
	case 26: // load16: sload16
	case 21: // load8: uload8
	case 20: // load8: sload8
	case 12: // any: riv
	case 9: // rlv: r
	case 7: // riv: r
	case 6: // szp: szpr
	case 5: // r: szpr
	case 4: // cz: czr
	case 3: // r: czr
	case 1: // stm: r
		if (kidnumber == 0)  return p;
		break;
	case 456: // stm: CLEAR_FLOATING_POINT_STATE
	case 455: // double_load: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
	case 454: // float_load: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
	case 453: // r: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
	case 379: // r: GET_TIME_BASE
	case 371: // stm: RETURN(LONG_CONSTANT)
	case 369: // stm: RETURN(INT_CONSTANT)
	case 368: // stm: RETURN(NULL)
	case 367: // stm: PAUSE
	case 366: // stm: READ_CEILING
	case 365: // stm: WRITE_FLOOR
	case 363: // stm: GOTO
	case 270: // r: GET_CURRENT_PROCESSOR
	case 269: // r: LONG_MOVE(LONG_CONSTANT)
	case 70: // stm: TRAP
	case 68: // r: GET_CAUGHT_EXCEPTION
	case 67: // stm: IR_PROLOGUE
	case 65: // r: GUARD_COMBINE
	case 64: // r: GUARD_MOVE
	case 63: // stm: NOP
	case 62: // stm: RESOLVE
	case 60: // stm: YIELDPOINT_BACKEDGE
	case 59: // stm: YIELDPOINT_EPILOGUE
	case 58: // stm: YIELDPOINT_PROLOGUE
	case 57: // stm: UNINT_END
	case 56: // stm: UNINT_BEGIN
	case 55: // stm: IG_PATCH_POINT
	case 14: // any: LONG_CONSTANT
	case 13: // any: ADDRESS_CONSTANT
	case 11: // any: NULL
	case 10: // rlv: LONG_CONSTANT
	case 8: // riv: INT_CONSTANT
	case 2: // r: REGISTER
		break;
	case 462: // stm: DOUBLE_IFCMP(double_load,r)
	case 461: // stm: DOUBLE_IFCMP(r,double_load)
	case 460: // stm: DOUBLE_IFCMP(r,r)
	case 459: // stm: FLOAT_IFCMP(float_load,r)
	case 458: // stm: FLOAT_IFCMP(r,float_load)
	case 457: // stm: FLOAT_IFCMP(r,r)
	case 428: // float_load: FLOAT_ALOAD(riv,riv)
	case 427: // r: FLOAT_ALOAD(riv,riv)
	case 426: // float_load: FLOAT_LOAD(riv,riv)
	case 425: // r: FLOAT_LOAD(riv,riv)
	case 424: // double_load: DOUBLE_ALOAD(riv,riv)
	case 423: // r: DOUBLE_ALOAD(riv,riv)
	case 422: // double_load: DOUBLE_LOAD(riv,riv)
	case 421: // r: DOUBLE_LOAD(riv,riv)
	case 416: // r: DOUBLE_REM(r,r)
	case 415: // r: FLOAT_REM(r,r)
	case 410: // r: DOUBLE_DIV(r,double_load)
	case 409: // r: DOUBLE_DIV(r,r)
	case 408: // r: FLOAT_DIV(r,float_load)
	case 407: // r: FLOAT_DIV(r,r)
	case 406: // r: DOUBLE_MUL(double_load,r)
	case 405: // r: DOUBLE_MUL(r,double_load)
	case 404: // r: DOUBLE_MUL(r,r)
	case 403: // r: FLOAT_MUL(float_load,r)
	case 402: // r: FLOAT_MUL(r,float_load)
	case 401: // r: FLOAT_MUL(r,r)
	case 400: // r: DOUBLE_SUB(r,double_load)
	case 399: // r: DOUBLE_SUB(r,r)
	case 398: // r: FLOAT_SUB(r,float_load)
	case 397: // r: FLOAT_SUB(r,r)
	case 396: // r: DOUBLE_ADD(double_load,r)
	case 395: // r: DOUBLE_ADD(r,double_load)
	case 394: // r: DOUBLE_ADD(r,r)
	case 393: // r: FLOAT_ADD(float_load,r)
	case 392: // r: FLOAT_ADD(r,float_load)
	case 391: // r: FLOAT_ADD(r,r)
	case 380: // stm: YIELDPOINT_OSR(any,any)
	case 376: // r: SYSCALL(r,any)
	case 372: // r: CALL(r,any)
	case 362: // r: LONG_CMP(rlv,rlv)
	case 361: // stm: LONG_IFCMP(rlv,rlv)
	case 360: // stm: INT_IFCMP2(riv,load32)
	case 359: // stm: INT_IFCMP2(load32,riv)
	case 358: // stm: INT_IFCMP2(r,riv)
	case 352: // stm: INT_IFCMP(r,load32)
	case 351: // stm: INT_IFCMP(load32,riv)
	case 349: // stm: INT_IFCMP(r,uload8)
	case 348: // stm: INT_IFCMP(uload8,r)
	case 345: // stm: INT_IFCMP(r,riv)
	case 303: // r: PREPARE_LONG(riv,riv)
	case 300: // r: PREPARE_INT(address1reg,address1scaledreg)
	case 299: // r: PREPARE_INT(address1scaledreg,address1reg)
	case 298: // r: PREPARE_INT(address1scaledreg,r)
	case 297: // r: PREPARE_INT(r,address1scaledreg)
	case 296: // r: PREPARE_INT(riv,riv)
	case 295: // r: LONG_ALOAD(riv,riv)
	case 294: // r: LONG_LOAD(riv,riv)
	case 293: // r: INT_ALOAD(riv,riv)
	case 291: // r: INT_LOAD(address1reg,address1scaledreg)
	case 290: // r: INT_LOAD(address1scaledreg,address1reg)
	case 289: // r: INT_LOAD(address1scaledreg,riv)
	case 288: // r: INT_LOAD(riv,address1scaledreg)
	case 287: // r: INT_LOAD(riv,riv)
	case 282: // r: USHORT_ALOAD(riv,riv)
	case 281: // r: USHORT_LOAD(riv,riv)
	case 280: // r: SHORT_ALOAD(riv,riv)
	case 279: // r: SHORT_LOAD(riv,riv)
	case 274: // r: UBYTE_ALOAD(riv,riv)
	case 273: // r: UBYTE_LOAD(riv,riv)
	case 272: // r: BYTE_ALOAD(riv,riv)
	case 271: // r: BYTE_LOAD(riv,riv)
	case 220: // r: LONG_XOR(r,rlv)
	case 219: // r: LONG_OR(rlv,rlv)
	case 218: // r: LONG_AND(rlv,rlv)
	case 216: // r: LONG_USHR(rlv,riv)
	case 214: // r: LONG_SHR(rlv,riv)
	case 212: // r: LONG_SHL(rlv,riv)
	case 210: // r: LONG_MUL(rlv,rlv)
	case 209: // r: LONG_SUB(rlv,rlv)
	case 208: // r: LONG_ADD(r,rlv)
	case 200: // szpr: INT_XOR(load32,riv)
	case 199: // szpr: INT_XOR(riv,load32)
	case 198: // szpr: INT_XOR(riv,riv)
	case 193: // szpr: INT_OR(load32,riv)
	case 192: // szpr: INT_OR(riv,load32)
	case 191: // szpr: INT_OR(riv,riv)
	case 186: // szp: INT_AND(riv,load8_16_32)
	case 185: // szp: INT_AND(load8_16_32,riv)
	case 184: // szpr: INT_AND(load32,riv)
	case 183: // szpr: INT_AND(riv,load32)
	case 182: // szp: INT_AND(r,riv)
	case 181: // szpr: INT_AND(riv,riv)
	case 167: // szpr: INT_USHR(riv,riv)
	case 160: // szpr: INT_SHR(riv,riv)
	case 151: // szpr: INT_SHL(riv,riv)
	case 146: // r: INT_REM(riv,load32)
	case 145: // r: INT_REM(riv,riv)
	case 144: // r: INT_DIV(riv,load32)
	case 143: // r: INT_DIV(riv,riv)
	case 142: // r: INT_MUL(load32,riv)
	case 141: // r: INT_MUL(riv,load32)
	case 140: // r: INT_MUL(riv,riv)
	case 135: // czr: INT_SUB(load32,riv)
	case 134: // czr: INT_SUB(riv,load32)
	case 133: // r: INT_SUB(load32,r)
	case 132: // r: INT_SUB(riv,r)
	case 131: // czr: INT_SUB(riv,r)
	case 126: // czr: INT_ADD(load32,riv)
	case 125: // czr: INT_ADD(riv,load32)
	case 124: // r: INT_ADD(r,riv)
	case 123: // czr: INT_ADD(r,riv)
	case 104: // boolcmp: BOOLEAN_CMP_LONG(rlv,rlv)
	case 103: // r: BOOLEAN_CMP_LONG(rlv,rlv)
	case 100: // boolcmp: BOOLEAN_CMP_INT(riv,load32)
	case 99: // r: BOOLEAN_CMP_INT(r,load32)
	case 98: // boolcmp: BOOLEAN_CMP_INT(load32,riv)
	case 97: // r: BOOLEAN_CMP_INT(load32,riv)
	case 80: // boolcmp: BOOLEAN_CMP_INT(r,riv)
	case 79: // r: BOOLEAN_CMP_INT(r,riv)
	case 75: // stm: TRAP_IF(riv,load32)
	case 74: // stm: TRAP_IF(load32,riv)
	case 73: // stm: TRAP_IF(r,r)
	case 52: // r: INT_ADD(address1reg,address1scaledreg)
	case 51: // r: INT_ADD(address1scaledreg,address1reg)
	case 50: // r: INT_ADD(r,address1scaledreg)
	case 49: // r: INT_ADD(address1scaledreg,r)
	case 48: // address: INT_ADD(address1reg,address1scaledreg)
	case 47: // address: INT_ADD(address1scaledreg,address1reg)
	case 45: // address: INT_ADD(address1scaledreg,r)
	case 44: // address: INT_ADD(r,address1scaledreg)
	case 41: // address: INT_ADD(r,r)
	case 35: // load64: LONG_ALOAD(riv,riv)
	case 34: // load64: LONG_LOAD(riv,riv)
	case 29: // load32: INT_ALOAD(riv,riv)
	case 28: // load32: INT_LOAD(riv,riv)
	case 25: // uload16: USHORT_ALOAD(riv,riv)
	case 24: // uload16: USHORT_LOAD(riv,riv)
	case 23: // sload16: SHORT_ALOAD(riv,riv)
	case 22: // sload16: SHORT_LOAD(riv,riv)
	case 19: // uload8: UBYTE_ALOAD(riv,riv)
	case 18: // uload8: UBYTE_LOAD(riv,riv)
	case 17: // sload8: BYTE_ALOAD(riv,riv)
	case 16: // sload8: BYTE_LOAD(riv,riv)
	case 15: // any: OTHER_OPERAND(any,any)
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2;
		break;
	case 452: // double_load: LONG_BITS_AS_DOUBLE(load64)
	case 451: // r: LONG_BITS_AS_DOUBLE(rlv)
	case 450: // float_load: INT_BITS_AS_FLOAT(load32)
	case 449: // r: INT_BITS_AS_FLOAT(riv)
	case 448: // load64: DOUBLE_AS_LONG_BITS(double_load)
	case 447: // r: DOUBLE_AS_LONG_BITS(r)
	case 446: // load32: FLOAT_AS_INT_BITS(float_load)
	case 445: // r: FLOAT_AS_INT_BITS(r)
	case 444: // r: DOUBLE_2LONG(r)
	case 443: // r: DOUBLE_2INT(r)
	case 442: // r: FLOAT_2LONG(r)
	case 441: // r: FLOAT_2INT(r)
	case 440: // r: DOUBLE_2FLOAT(double_load)
	case 439: // r: DOUBLE_2FLOAT(r)
	case 438: // r: FLOAT_2DOUBLE(float_load)
	case 437: // r: FLOAT_2DOUBLE(r)
	case 436: // r: INT_2DOUBLE(load32)
	case 435: // r: INT_2DOUBLE(riv)
	case 434: // r: INT_2FLOAT(load32)
	case 433: // r: INT_2FLOAT(riv)
	case 420: // r: DOUBLE_MOVE(r)
	case 419: // r: FLOAT_MOVE(r)
	case 418: // r: LONG_2DOUBLE(r)
	case 417: // r: LONG_2FLOAT(r)
	case 414: // r: DOUBLE_SQRT(r)
	case 413: // r: FLOAT_SQRT(r)
	case 412: // r: DOUBLE_NEG(r)
	case 411: // r: FLOAT_NEG(r)
	case 370: // stm: RETURN(r)
	case 364: // stm: PREFETCH(r)
	case 357: // stm: INT_IFCMP(bittest,INT_CONSTANT)
	case 356: // stm: INT_IFCMP(szp,INT_CONSTANT)
	case 355: // stm: INT_IFCMP(cz,INT_CONSTANT)
	case 354: // stm: INT_IFCMP(boolcmp,INT_CONSTANT)
	case 353: // stm: INT_IFCMP(boolcmp,INT_CONSTANT)
	case 350: // stm: INT_IFCMP(sload16,INT_CONSTANT)
	case 347: // stm: INT_IFCMP(load8,INT_CONSTANT)
	case 346: // stm: INT_IFCMP(r,INT_CONSTANT)
	case 301: // r: PREPARE_INT(address,INT_CONSTANT)
	case 292: // r: INT_LOAD(address,INT_CONSTANT)
	case 285: // r: INT_2USHORT(load16_32)
	case 284: // r: INT_2USHORT(load16_32)
	case 283: // r: INT_AND(load16_32,INT_CONSTANT)
	case 277: // r: INT_2BYTE(load8_16_32)
	case 276: // r: INT_AND(load8_16_32,INT_CONSTANT)
	case 275: // uload8: INT_AND(load8_16_32,INT_CONSTANT)
	case 268: // r: LONG_MOVE(r)
	case 267: // load64: LONG_MOVE(load64)
	case 266: // load32: INT_MOVE(load32)
	case 265: // load16: INT_MOVE(load16)
	case 264: // uload16: INT_MOVE(uload16)
	case 263: // sload16: INT_MOVE(sload16)
	case 262: // load8: INT_MOVE(load8)
	case 261: // uload8: INT_MOVE(uload8)
	case 260: // sload8: INT_MOVE(sload8)
	case 259: // address: INT_MOVE(address)
	case 258: // address1scaledreg: INT_MOVE(address1scaledreg)
	case 257: // address1reg: INT_MOVE(address1reg)
	case 256: // szp: INT_MOVE(szp)
	case 255: // szpr: INT_MOVE(szpr)
	case 254: // cz: INT_MOVE(cz)
	case 253: // czr: INT_MOVE(czr)
	case 252: // r: INT_MOVE(riv)
	case 245: // r: LONG_2INT(load64)
	case 242: // r: LONG_2INT(r)
	case 241: // r: ADDR_2LONG(load32)
	case 240: // r: ADDR_2LONG(r)
	case 235: // r: INT_2LONG(load32)
	case 234: // r: INT_2LONG(r)
	case 231: // r: INT_2SHORT(load16_32)
	case 230: // r: INT_2SHORT(r)
	case 227: // r: INT_2USHORT(load16_32)
	case 226: // szpr: INT_2USHORT(r)
	case 223: // r: INT_2BYTE(load8_16_32)
	case 222: // r: INT_2BYTE(r)
	case 221: // r: LONG_NOT(r)
	case 211: // r: LONG_NEG(r)
	case 205: // r: INT_NOT(r)
	case 168: // szpr: INT_USHR(riv,INT_CONSTANT)
	case 161: // szpr: INT_SHR(riv,INT_CONSTANT)
	case 153: // r: INT_SHL(r,INT_CONSTANT)
	case 152: // szpr: INT_SHL(r,INT_CONSTANT)
	case 147: // szpr: INT_NEG(r)
	case 110: // r: CMP_CMOV(load32,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
	case 109: // r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
	case 108: // r: CMP_CMOV(load32,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
	case 107: // r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
	case 96: // boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
	case 95: // r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
	case 94: // boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
	case 93: // r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
	case 92: // boolcmp: BOOLEAN_CMP_INT(bittest,INT_CONSTANT)
	case 91: // r: BOOLEAN_CMP_INT(bittest,INT_CONSTANT)
	case 90: // boolcmp: BOOLEAN_CMP_INT(szp,INT_CONSTANT)
	case 89: // r: BOOLEAN_CMP_INT(szp,INT_CONSTANT)
	case 88: // boolcmp: BOOLEAN_CMP_INT(cz,INT_CONSTANT)
	case 87: // r: BOOLEAN_CMP_INT(cz,INT_CONSTANT)
	case 86: // r: BOOLEAN_CMP_INT(load32,INT_CONSTANT)
	case 85: // r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
	case 84: // r: BOOLEAN_CMP_INT(load32,INT_CONSTANT)
	case 83: // r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
	case 82: // boolcmp: BOOLEAN_CMP_INT(r,INT_CONSTANT)
	case 81: // r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
	case 76: // r: BOOLEAN_NOT(r)
	case 72: // stm: TRAP_IF(r,LONG_CONSTANT)
	case 71: // stm: TRAP_IF(r,INT_CONSTANT)
	case 69: // stm: SET_CAUGHT_EXCEPTION(r)
	case 66: // stm: NULL_CHECK(riv)
	case 61: // stm: LOWTABLESWITCH(r)
	case 54: // r: INT_MOVE(address)
	case 53: // r: INT_ADD(address,INT_CONSTANT)
	case 46: // address: INT_ADD(address1scaledreg,INT_CONSTANT)
	case 43: // address1scaledreg: INT_ADD(address1scaledreg,INT_CONSTANT)
	case 42: // address1reg: INT_ADD(address1reg,INT_CONSTANT)
	case 40: // address1reg: INT_MOVE(r)
	case 39: // address1reg: INT_ADD(r,INT_CONSTANT)
	case 38: // address1scaledreg: INT_SHL(r,INT_CONSTANT)
		if (kidnumber == 0)  return p.child1;
		break;
	case 207: // stm: INT_ASTORE(INT_NOT(INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 206: // stm: INT_STORE(INT_NOT(INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 172: // stm: INT_ASTORE(INT_USHR(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 170: // stm: INT_STORE(INT_USHR(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 165: // stm: INT_ASTORE(INT_SHR(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 163: // stm: INT_STORE(INT_SHR(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 158: // stm: INT_ASTORE(INT_SHL(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 156: // stm: INT_STORE(INT_SHL(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 149: // stm: INT_ASTORE(INT_NEG(INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 148: // stm: INT_STORE(INT_NEG(INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 78: // stm: BYTE_ASTORE(BOOLEAN_NOT(UBYTE_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 77: // stm: BYTE_STORE(BOOLEAN_NOT(UBYTE_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		if (kidnumber == 0)  return p.child1.child1.child1;
		if (kidnumber == 1)  return p.child1.child1.child2;
		if (kidnumber == 2)  return p.child2.child1;
		if (kidnumber == 3)  return p.child2.child2;
		break;
	case 484: // stm: LONG_STORE(load64,OTHER_OPERAND(riv,riv))
	case 483: // stm: LONG_ASTORE(load64,OTHER_OPERAND(riv,riv))
	case 474: // r: FCMP_FCMOV(r,OTHER_OPERAND(double_load,any))
	case 473: // r: FCMP_FCMOV(r,OTHER_OPERAND(float_load,any))
	case 468: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,any))
	case 467: // r: FCMP_CMOV(double_load,OTHER_OPERAND(r,any))
	case 466: // r: FCMP_CMOV(float_load,OTHER_OPERAND(r,any))
	case 465: // r: FCMP_CMOV(r,OTHER_OPERAND(double_load,any))
	case 464: // r: FCMP_CMOV(r,OTHER_OPERAND(float_load,any))
	case 463: // r: FCMP_CMOV(r,OTHER_OPERAND(r,any))
	case 432: // stm: FLOAT_ASTORE(r,OTHER_OPERAND(riv,riv))
	case 431: // stm: FLOAT_STORE(r,OTHER_OPERAND(riv,riv))
	case 430: // stm: DOUBLE_ASTORE(r,OTHER_OPERAND(riv,riv))
	case 429: // stm: DOUBLE_STORE(r,OTHER_OPERAND(riv,riv))
	case 343: // stm: LONG_ASTORE(r,OTHER_OPERAND(riv,riv))
	case 341: // stm: LONG_STORE(r,OTHER_OPERAND(riv,riv))
	case 340: // stm: INT_ASTORE(riv,OTHER_OPERAND(riv,riv))
	case 338: // stm: INT_STORE(riv,OTHER_OPERAND(address1reg,address1scaledreg))
	case 337: // stm: INT_STORE(riv,OTHER_OPERAND(address1scaledreg,address1reg))
	case 336: // stm: INT_STORE(riv,OTHER_OPERAND(address1scaledreg,riv))
	case 335: // stm: INT_STORE(riv,OTHER_OPERAND(riv,address1scaledreg))
	case 334: // stm: INT_STORE(riv,OTHER_OPERAND(riv,riv))
	case 333: // stm: SHORT_ASTORE(load16,OTHER_OPERAND(riv,riv))
	case 332: // stm: SHORT_ASTORE(riv,OTHER_OPERAND(riv,riv))
	case 331: // stm: SHORT_STORE(load16,OTHER_OPERAND(riv,riv))
	case 330: // stm: SHORT_STORE(riv,OTHER_OPERAND(riv,riv))
	case 329: // stm: BYTE_ASTORE(load8,OTHER_OPERAND(riv,riv))
	case 328: // stm: BYTE_ASTORE(riv,OTHER_OPERAND(riv,riv))
	case 327: // stm: BYTE_STORE(load8,OTHER_OPERAND(riv,riv))
	case 326: // stm: BYTE_STORE(riv,OTHER_OPERAND(riv,riv))
	case 122: // r: LCMP_CMOV(r,OTHER_OPERAND(rlv,any))
	case 116: // r: CMP_CMOV(riv,OTHER_OPERAND(load32,any))
	case 115: // r: CMP_CMOV(load32,OTHER_OPERAND(riv,any))
	case 113: // r: CMP_CMOV(riv,OTHER_OPERAND(uload8,any))
	case 112: // r: CMP_CMOV(uload8,OTHER_OPERAND(riv,any))
	case 105: // r: CMP_CMOV(r,OTHER_OPERAND(riv,any))
	case 102: // stm: BYTE_ASTORE(boolcmp,OTHER_OPERAND(riv,riv))
	case 101: // stm: BYTE_STORE(boolcmp,OTHER_OPERAND(riv,riv))
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2.child1;
		if (kidnumber == 2)  return p.child2.child2;
		break;
	case 121: // r: CMP_CMOV(szp,OTHER_OPERAND(INT_CONSTANT,any))
	case 120: // r: CMP_CMOV(cz,OTHER_OPERAND(INT_CONSTANT,any))
	case 119: // r: CMP_CMOV(bittest,OTHER_OPERAND(INT_CONSTANT,any))
	case 118: // r: CMP_CMOV(boolcmp,OTHER_OPERAND(INT_CONSTANT,any))
	case 117: // r: CMP_CMOV(boolcmp,OTHER_OPERAND(INT_CONSTANT,any))
	case 114: // r: CMP_CMOV(sload16,OTHER_OPERAND(INT_CONSTANT,any))
	case 111: // r: CMP_CMOV(load8,OTHER_OPERAND(INT_CONSTANT,any))
	case 106: // r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,any))
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2.child2;
		break;
	case 203: // stm: INT_ASTORE(INT_XOR(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 201: // stm: INT_STORE(INT_XOR(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 196: // stm: INT_ASTORE(INT_OR(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 194: // stm: INT_STORE(INT_OR(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 189: // stm: INT_ASTORE(INT_AND(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 187: // stm: INT_STORE(INT_AND(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 138: // stm: INT_ASTORE(INT_SUB(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 136: // stm: INT_STORE(INT_SUB(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 129: // stm: INT_ASTORE(INT_ADD(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 127: // stm: INT_STORE(INT_ADD(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		if (kidnumber == 0)  return p.child1.child1.child1;
		if (kidnumber == 1)  return p.child1.child1.child2;
		if (kidnumber == 2)  return p.child1.child2;
		if (kidnumber == 3)  return p.child2.child1;
		if (kidnumber == 4)  return p.child2.child2;
		break;
	case 204: // stm: INT_ASTORE(INT_XOR(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 202: // stm: INT_STORE(INT_XOR(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 197: // stm: INT_ASTORE(INT_OR(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 195: // stm: INT_STORE(INT_OR(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 190: // stm: INT_ASTORE(INT_AND(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 188: // stm: INT_STORE(INT_AND(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 139: // stm: INT_ASTORE(INT_SUB(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 137: // stm: INT_STORE(INT_SUB(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 130: // stm: INT_ASTORE(INT_ADD(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 128: // stm: INT_STORE(INT_ADD(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child1.child2.child1;
		if (kidnumber == 2)  return p.child1.child2.child2;
		if (kidnumber == 3)  return p.child2.child1;
		if (kidnumber == 4)  return p.child2.child2;
		break;
	case 339: // stm: INT_STORE(riv,OTHER_OPERAND(address,INT_CONSTANT))
	case 217: // r: LONG_USHR(rlv,INT_AND(riv,INT_CONSTANT))
	case 215: // r: LONG_SHR(rlv,INT_AND(riv,INT_CONSTANT))
	case 213: // r: LONG_SHL(rlv,INT_AND(riv,INT_CONSTANT))
	case 166: // szpr: INT_USHR(riv,INT_AND(r,INT_CONSTANT))
	case 159: // szpr: INT_SHR(riv,INT_AND(r,INT_CONSTANT))
	case 150: // szpr: INT_SHL(riv,INT_AND(r,INT_CONSTANT))
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2.child1;
		break;
	case 386: // bittest: INT_AND(INT_SHR(r,INT_CONSTANT),INT_CONSTANT)
	case 383: // bittest: INT_AND(INT_USHR(r,INT_CONSTANT),INT_CONSTANT)
	case 286: // r: INT_USHR(INT_SHL(load16_32,INT_CONSTANT),INT_CONSTANT)
	case 278: // r: INT_USHR(INT_SHL(load8_16_32,INT_CONSTANT),INT_CONSTANT)
	case 251: // load32: LONG_2INT(LONG_SHR(load64,INT_CONSTANT))
	case 250: // load32: LONG_2INT(LONG_USHR(load64,INT_CONSTANT))
	case 249: // r: LONG_2INT(LONG_SHR(load64,INT_CONSTANT))
	case 248: // r: LONG_2INT(LONG_USHR(load64,INT_CONSTANT))
	case 247: // r: LONG_2INT(LONG_SHR(r,INT_CONSTANT))
	case 246: // r: LONG_2INT(LONG_USHR(r,INT_CONSTANT))
	case 239: // r: LONG_SHL(INT_2LONG(load64),INT_CONSTANT)
	case 238: // r: LONG_SHL(INT_2LONG(r),INT_CONSTANT)
	case 237: // r: LONG_AND(INT_2LONG(load32),LONG_CONSTANT)
	case 236: // r: LONG_AND(INT_2LONG(r),LONG_CONSTANT)
	case 154: // szpr: INT_SHL(INT_SHR(r,INT_CONSTANT),INT_CONSTANT)
		if (kidnumber == 0)  return p.child1.child1;
		break;
	case 171: // stm: INT_ASTORE(INT_USHR(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 169: // stm: INT_STORE(INT_USHR(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 164: // stm: INT_ASTORE(INT_SHR(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 162: // stm: INT_STORE(INT_SHR(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 157: // stm: INT_ASTORE(INT_SHL(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 155: // stm: INT_STORE(INT_SHL(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
		if (kidnumber == 0)  return p.child1.child1.child1;
		if (kidnumber == 1)  return p.child1.child1.child2;
		if (kidnumber == 2)  return p.child1.child2.child1;
		if (kidnumber == 3)  return p.child2.child1;
		if (kidnumber == 4)  return p.child2.child2;
		break;
	case 176: // r: INT_OR(INT_USHR(r,INT_CONSTANT),INT_SHL(r,INT_CONSTANT))
	case 175: // r: INT_OR(INT_SHL(r,INT_CONSTANT),INT_USHR(r,INT_CONSTANT))
	case 174: // r: INT_OR(INT_USHR(r,INT_CONSTANT),INT_SHL(r,INT_CONSTANT))
	case 173: // r: INT_OR(INT_SHL(r,INT_CONSTANT),INT_USHR(r,INT_CONSTANT))
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child2.child1;
		break;
	case 180: // r: INT_OR(INT_USHR(r,INT_AND(r,INT_CONSTANT)),INT_SHL(r,INT_AND(INT_NEG(r),INT_CONSTANT)))
	case 177: // r: INT_OR(INT_SHL(r,INT_AND(r,INT_CONSTANT)),INT_USHR(r,INT_AND(INT_NEG(r),INT_CONSTANT)))
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child1.child2.child1;
		if (kidnumber == 2)  return p.child2.child1;
		if (kidnumber == 3)  return p.child2.child2.child1.child1;
		break;
	case 179: // r: INT_OR(INT_SHL(r,INT_AND(INT_NEG(r),INT_CONSTANT)),INT_USHR(r,INT_AND(r,INT_CONSTANT)))
	case 178: // r: INT_OR(INT_USHR(r,INT_AND(INT_NEG(r),INT_CONSTANT)),INT_SHL(r,INT_AND(r,INT_CONSTANT)))
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child1.child2.child1.child1;
		if (kidnumber == 2)  return p.child2.child1;
		if (kidnumber == 3)  return p.child2.child2.child1;
		break;
	case 244: // stm: INT_ASTORE(LONG_2INT(r),OTHER_OPERAND(riv,riv))
	case 243: // stm: INT_STORE(LONG_2INT(r),OTHER_OPERAND(riv,riv))
	case 233: // stm: SHORT_ASTORE(INT_2SHORT(r),OTHER_OPERAND(riv,riv))
	case 232: // stm: SHORT_STORE(INT_2SHORT(r),OTHER_OPERAND(riv,riv))
	case 229: // stm: SHORT_ASTORE(INT_2USHORT(r),OTHER_OPERAND(riv,riv))
	case 228: // stm: SHORT_STORE(INT_2USHORT(r),OTHER_OPERAND(riv,riv))
	case 225: // stm: BYTE_ASTORE(INT_2BYTE(r),OTHER_OPERAND(riv,riv))
	case 224: // stm: BYTE_STORE(INT_2BYTE(r),OTHER_OPERAND(riv,riv))
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child2.child1;
		if (kidnumber == 2)  return p.child2.child2;
		break;
	case 378: // r: SYSCALL(INT_CONSTANT,any)
	case 375: // r: CALL(INT_CONSTANT,any)
	case 373: // r: CALL(BRANCH_TARGET,any)
	case 302: // r: PREPARE_INT(INT_CONSTANT,address)
		if (kidnumber == 0)  return p.child2;
		break;
	case 472: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(double_load,r)))
	case 471: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(float_load,r)))
	case 470: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(r,double_load)))
	case 469: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(r,float_load)))
	case 325: // r: ATTEMPT_LONG(riv,OTHER_OPERAND(riv,OTHER_OPERAND(rlv,rlv)))
	case 308: // r: ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv)))
	case 307: // r: ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv)))
	case 306: // r: ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv)))
	case 305: // r: ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv)))
	case 304: // r: ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv)))
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2.child1;
		if (kidnumber == 2)  return p.child2.child2.child1;
		if (kidnumber == 3)  return p.child2.child2.child2;
		break;
	case 309: // r: ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv)))
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2.child2.child1;
		if (kidnumber == 2)  return p.child2.child2.child2;
		break;
	case 310: // r: ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv)))
		if (kidnumber == 0)  return p.child2.child1;
		if (kidnumber == 1)  return p.child2.child2.child1;
		if (kidnumber == 2)  return p.child2.child2.child2;
		break;
	case 322: // stm: INT_IFCMP(ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 321: // stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 320: // stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 319: // stm: INT_IFCMP(ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 318: // stm: INT_IFCMP(ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 315: // stm: INT_IFCMP(ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 314: // stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 313: // stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 312: // stm: INT_IFCMP(ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 311: // stm: INT_IFCMP(ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child1.child2.child1;
		if (kidnumber == 2)  return p.child1.child2.child2.child1;
		if (kidnumber == 3)  return p.child1.child2.child2.child2;
		break;
	case 323: // stm: INT_IFCMP(ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 316: // stm: INT_IFCMP(ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child1.child2.child2.child1;
		if (kidnumber == 2)  return p.child1.child2.child2.child2;
		break;
	case 324: // stm: INT_IFCMP(ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 317: // stm: INT_IFCMP(ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		if (kidnumber == 0)  return p.child1.child2.child1;
		if (kidnumber == 1)  return p.child1.child2.child2.child1;
		if (kidnumber == 2)  return p.child1.child2.child2.child2;
		break;
	case 344: // stm: LONG_ASTORE(LONG_CONSTANT,OTHER_OPERAND(riv,riv))
	case 342: // stm: LONG_STORE(LONG_CONSTANT,OTHER_OPERAND(riv,riv))
		if (kidnumber == 0)  return p.child2.child1;
		if (kidnumber == 1)  return p.child2.child2;
		break;
	case 377: // r: SYSCALL(INT_LOAD(riv,riv),any)
	case 374: // r: CALL(INT_LOAD(riv,riv),any)
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child1.child2;
		if (kidnumber == 2)  return p.child2;
		break;
	case 385: // bittest: INT_AND(INT_SHR(load32,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
	case 384: // bittest: INT_AND(INT_SHR(r,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
	case 382: // bittest: INT_AND(INT_USHR(load32,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
	case 381: // bittest: INT_AND(INT_USHR(r,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
		if (kidnumber == 0)  return p.child1.child1;
		if (kidnumber == 1)  return p.child1.child2.child1;
		break;
	case 388: // bittest: INT_AND(INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)),load32)
	case 387: // bittest: INT_AND(INT_SHL(INT_CONSTANT,INT_AND(riv,INT_CONSTANT)),r)
		if (kidnumber == 0)  return p.child1.child2.child1;
		if (kidnumber == 1)  return p.child2;
		break;
	case 390: // bittest: INT_AND(load32,INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)))
	case 389: // bittest: INT_AND(r,INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)))
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2.child2.child1;
		break;
	case 479: // r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,DOUBLE_NEG(r))))
	case 475: // r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,FLOAT_NEG(r))))
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2.child2.child1;
		if (kidnumber == 2)  return p.child2.child2.child2.child1;
		break;
	case 480: // r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(DOUBLE_NEG(r),r)))
	case 476: // r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(FLOAT_NEG(r),r)))
		if (kidnumber == 0)  return p.child1;
		if (kidnumber == 1)  return p.child2.child2.child1.child1;
		if (kidnumber == 2)  return p.child2.child2.child2;
		break;
	case 481: // r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(DOUBLE_NEG(r),r)))
	case 477: // r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(FLOAT_NEG(r),r)))
		if (kidnumber == 0)  return p.child2.child1;
		if (kidnumber == 1)  return p.child2.child2.child1.child1;
		if (kidnumber == 2)  return p.child2.child2.child2;
		break;
	case 482: // r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(r,DOUBLE_NEG(r))))
	case 478: // r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(r,FLOAT_NEG(r))))
		if (kidnumber == 0)  return p.child2.child1;
		if (kidnumber == 1)  return p.child2.child2.child1;
		if (kidnumber == 2)  return p.child2.child2.child2.child1;
		break;
	}
	throw new OptimizingCompilerException("BURS","Bad rule number ",Integer.toString(eruleno));
} else return null;
}

static void mark_kids(BURS_TreeNode p, int eruleno)
	 {
	byte[] ntsrule = nts[eruleno];
	switch (eruleno) {
	case 37: // address: address1scaledreg
	case 36: // address1scaledreg: address1reg
	case 33: // load8_16_32: load8
	case 32: // load8_16_32: load16_32
	case 31: // load16_32: load32
	case 30: // load16_32: load16
	case 27: // load16: uload16
	case 26: // load16: sload16
	case 21: // load8: uload8
	case 20: // load8: sload8
	case 12: // any: riv
	case 9: // rlv: r
	case 7: // riv: r
	case 6: // szp: szpr
	case 5: // r: szpr
	case 4: // cz: czr
	case 3: // r: czr
	case 1: // stm: r
		mark(p, ntsrule[0]);
		break;
	case 456: // stm: CLEAR_FLOATING_POINT_STATE
	case 455: // double_load: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
	case 454: // float_load: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
	case 453: // r: MATERIALIZE_FP_CONSTANT(INT_CONSTANT)
	case 379: // r: GET_TIME_BASE
	case 371: // stm: RETURN(LONG_CONSTANT)
	case 369: // stm: RETURN(INT_CONSTANT)
	case 368: // stm: RETURN(NULL)
	case 367: // stm: PAUSE
	case 366: // stm: READ_CEILING
	case 365: // stm: WRITE_FLOOR
	case 363: // stm: GOTO
	case 270: // r: GET_CURRENT_PROCESSOR
	case 269: // r: LONG_MOVE(LONG_CONSTANT)
	case 70: // stm: TRAP
	case 68: // r: GET_CAUGHT_EXCEPTION
	case 67: // stm: IR_PROLOGUE
	case 65: // r: GUARD_COMBINE
	case 64: // r: GUARD_MOVE
	case 63: // stm: NOP
	case 62: // stm: RESOLVE
	case 60: // stm: YIELDPOINT_BACKEDGE
	case 59: // stm: YIELDPOINT_EPILOGUE
	case 58: // stm: YIELDPOINT_PROLOGUE
	case 57: // stm: UNINT_END
	case 56: // stm: UNINT_BEGIN
	case 55: // stm: IG_PATCH_POINT
	case 14: // any: LONG_CONSTANT
	case 13: // any: ADDRESS_CONSTANT
	case 11: // any: NULL
	case 10: // rlv: LONG_CONSTANT
	case 8: // riv: INT_CONSTANT
	case 2: // r: REGISTER
		break;
	case 462: // stm: DOUBLE_IFCMP(double_load,r)
	case 461: // stm: DOUBLE_IFCMP(r,double_load)
	case 460: // stm: DOUBLE_IFCMP(r,r)
	case 459: // stm: FLOAT_IFCMP(float_load,r)
	case 458: // stm: FLOAT_IFCMP(r,float_load)
	case 457: // stm: FLOAT_IFCMP(r,r)
	case 428: // float_load: FLOAT_ALOAD(riv,riv)
	case 427: // r: FLOAT_ALOAD(riv,riv)
	case 426: // float_load: FLOAT_LOAD(riv,riv)
	case 425: // r: FLOAT_LOAD(riv,riv)
	case 424: // double_load: DOUBLE_ALOAD(riv,riv)
	case 423: // r: DOUBLE_ALOAD(riv,riv)
	case 422: // double_load: DOUBLE_LOAD(riv,riv)
	case 421: // r: DOUBLE_LOAD(riv,riv)
	case 416: // r: DOUBLE_REM(r,r)
	case 415: // r: FLOAT_REM(r,r)
	case 410: // r: DOUBLE_DIV(r,double_load)
	case 409: // r: DOUBLE_DIV(r,r)
	case 408: // r: FLOAT_DIV(r,float_load)
	case 407: // r: FLOAT_DIV(r,r)
	case 406: // r: DOUBLE_MUL(double_load,r)
	case 405: // r: DOUBLE_MUL(r,double_load)
	case 404: // r: DOUBLE_MUL(r,r)
	case 403: // r: FLOAT_MUL(float_load,r)
	case 402: // r: FLOAT_MUL(r,float_load)
	case 401: // r: FLOAT_MUL(r,r)
	case 400: // r: DOUBLE_SUB(r,double_load)
	case 399: // r: DOUBLE_SUB(r,r)
	case 398: // r: FLOAT_SUB(r,float_load)
	case 397: // r: FLOAT_SUB(r,r)
	case 396: // r: DOUBLE_ADD(double_load,r)
	case 395: // r: DOUBLE_ADD(r,double_load)
	case 394: // r: DOUBLE_ADD(r,r)
	case 393: // r: FLOAT_ADD(float_load,r)
	case 392: // r: FLOAT_ADD(r,float_load)
	case 391: // r: FLOAT_ADD(r,r)
	case 380: // stm: YIELDPOINT_OSR(any,any)
	case 376: // r: SYSCALL(r,any)
	case 372: // r: CALL(r,any)
	case 362: // r: LONG_CMP(rlv,rlv)
	case 361: // stm: LONG_IFCMP(rlv,rlv)
	case 360: // stm: INT_IFCMP2(riv,load32)
	case 359: // stm: INT_IFCMP2(load32,riv)
	case 358: // stm: INT_IFCMP2(r,riv)
	case 352: // stm: INT_IFCMP(r,load32)
	case 351: // stm: INT_IFCMP(load32,riv)
	case 349: // stm: INT_IFCMP(r,uload8)
	case 348: // stm: INT_IFCMP(uload8,r)
	case 345: // stm: INT_IFCMP(r,riv)
	case 303: // r: PREPARE_LONG(riv,riv)
	case 300: // r: PREPARE_INT(address1reg,address1scaledreg)
	case 299: // r: PREPARE_INT(address1scaledreg,address1reg)
	case 298: // r: PREPARE_INT(address1scaledreg,r)
	case 297: // r: PREPARE_INT(r,address1scaledreg)
	case 296: // r: PREPARE_INT(riv,riv)
	case 295: // r: LONG_ALOAD(riv,riv)
	case 294: // r: LONG_LOAD(riv,riv)
	case 293: // r: INT_ALOAD(riv,riv)
	case 291: // r: INT_LOAD(address1reg,address1scaledreg)
	case 290: // r: INT_LOAD(address1scaledreg,address1reg)
	case 289: // r: INT_LOAD(address1scaledreg,riv)
	case 288: // r: INT_LOAD(riv,address1scaledreg)
	case 287: // r: INT_LOAD(riv,riv)
	case 282: // r: USHORT_ALOAD(riv,riv)
	case 281: // r: USHORT_LOAD(riv,riv)
	case 280: // r: SHORT_ALOAD(riv,riv)
	case 279: // r: SHORT_LOAD(riv,riv)
	case 274: // r: UBYTE_ALOAD(riv,riv)
	case 273: // r: UBYTE_LOAD(riv,riv)
	case 272: // r: BYTE_ALOAD(riv,riv)
	case 271: // r: BYTE_LOAD(riv,riv)
	case 220: // r: LONG_XOR(r,rlv)
	case 219: // r: LONG_OR(rlv,rlv)
	case 218: // r: LONG_AND(rlv,rlv)
	case 216: // r: LONG_USHR(rlv,riv)
	case 214: // r: LONG_SHR(rlv,riv)
	case 212: // r: LONG_SHL(rlv,riv)
	case 210: // r: LONG_MUL(rlv,rlv)
	case 209: // r: LONG_SUB(rlv,rlv)
	case 208: // r: LONG_ADD(r,rlv)
	case 200: // szpr: INT_XOR(load32,riv)
	case 199: // szpr: INT_XOR(riv,load32)
	case 198: // szpr: INT_XOR(riv,riv)
	case 193: // szpr: INT_OR(load32,riv)
	case 192: // szpr: INT_OR(riv,load32)
	case 191: // szpr: INT_OR(riv,riv)
	case 186: // szp: INT_AND(riv,load8_16_32)
	case 185: // szp: INT_AND(load8_16_32,riv)
	case 184: // szpr: INT_AND(load32,riv)
	case 183: // szpr: INT_AND(riv,load32)
	case 182: // szp: INT_AND(r,riv)
	case 181: // szpr: INT_AND(riv,riv)
	case 167: // szpr: INT_USHR(riv,riv)
	case 160: // szpr: INT_SHR(riv,riv)
	case 151: // szpr: INT_SHL(riv,riv)
	case 146: // r: INT_REM(riv,load32)
	case 145: // r: INT_REM(riv,riv)
	case 144: // r: INT_DIV(riv,load32)
	case 143: // r: INT_DIV(riv,riv)
	case 142: // r: INT_MUL(load32,riv)
	case 141: // r: INT_MUL(riv,load32)
	case 140: // r: INT_MUL(riv,riv)
	case 135: // czr: INT_SUB(load32,riv)
	case 134: // czr: INT_SUB(riv,load32)
	case 133: // r: INT_SUB(load32,r)
	case 132: // r: INT_SUB(riv,r)
	case 131: // czr: INT_SUB(riv,r)
	case 126: // czr: INT_ADD(load32,riv)
	case 125: // czr: INT_ADD(riv,load32)
	case 124: // r: INT_ADD(r,riv)
	case 123: // czr: INT_ADD(r,riv)
	case 104: // boolcmp: BOOLEAN_CMP_LONG(rlv,rlv)
	case 103: // r: BOOLEAN_CMP_LONG(rlv,rlv)
	case 100: // boolcmp: BOOLEAN_CMP_INT(riv,load32)
	case 99: // r: BOOLEAN_CMP_INT(r,load32)
	case 98: // boolcmp: BOOLEAN_CMP_INT(load32,riv)
	case 97: // r: BOOLEAN_CMP_INT(load32,riv)
	case 80: // boolcmp: BOOLEAN_CMP_INT(r,riv)
	case 79: // r: BOOLEAN_CMP_INT(r,riv)
	case 75: // stm: TRAP_IF(riv,load32)
	case 74: // stm: TRAP_IF(load32,riv)
	case 73: // stm: TRAP_IF(r,r)
	case 52: // r: INT_ADD(address1reg,address1scaledreg)
	case 51: // r: INT_ADD(address1scaledreg,address1reg)
	case 50: // r: INT_ADD(r,address1scaledreg)
	case 49: // r: INT_ADD(address1scaledreg,r)
	case 48: // address: INT_ADD(address1reg,address1scaledreg)
	case 47: // address: INT_ADD(address1scaledreg,address1reg)
	case 45: // address: INT_ADD(address1scaledreg,r)
	case 44: // address: INT_ADD(r,address1scaledreg)
	case 41: // address: INT_ADD(r,r)
	case 35: // load64: LONG_ALOAD(riv,riv)
	case 34: // load64: LONG_LOAD(riv,riv)
	case 29: // load32: INT_ALOAD(riv,riv)
	case 28: // load32: INT_LOAD(riv,riv)
	case 25: // uload16: USHORT_ALOAD(riv,riv)
	case 24: // uload16: USHORT_LOAD(riv,riv)
	case 23: // sload16: SHORT_ALOAD(riv,riv)
	case 22: // sload16: SHORT_LOAD(riv,riv)
	case 19: // uload8: UBYTE_ALOAD(riv,riv)
	case 18: // uload8: UBYTE_LOAD(riv,riv)
	case 17: // sload8: BYTE_ALOAD(riv,riv)
	case 16: // sload8: BYTE_LOAD(riv,riv)
	case 15: // any: OTHER_OPERAND(any,any)
		mark(p.child1, ntsrule[0]);
		mark(p.child2, ntsrule[1]);
		break;
	case 452: // double_load: LONG_BITS_AS_DOUBLE(load64)
	case 451: // r: LONG_BITS_AS_DOUBLE(rlv)
	case 450: // float_load: INT_BITS_AS_FLOAT(load32)
	case 449: // r: INT_BITS_AS_FLOAT(riv)
	case 448: // load64: DOUBLE_AS_LONG_BITS(double_load)
	case 447: // r: DOUBLE_AS_LONG_BITS(r)
	case 446: // load32: FLOAT_AS_INT_BITS(float_load)
	case 445: // r: FLOAT_AS_INT_BITS(r)
	case 444: // r: DOUBLE_2LONG(r)
	case 443: // r: DOUBLE_2INT(r)
	case 442: // r: FLOAT_2LONG(r)
	case 441: // r: FLOAT_2INT(r)
	case 440: // r: DOUBLE_2FLOAT(double_load)
	case 439: // r: DOUBLE_2FLOAT(r)
	case 438: // r: FLOAT_2DOUBLE(float_load)
	case 437: // r: FLOAT_2DOUBLE(r)
	case 436: // r: INT_2DOUBLE(load32)
	case 435: // r: INT_2DOUBLE(riv)
	case 434: // r: INT_2FLOAT(load32)
	case 433: // r: INT_2FLOAT(riv)
	case 420: // r: DOUBLE_MOVE(r)
	case 419: // r: FLOAT_MOVE(r)
	case 418: // r: LONG_2DOUBLE(r)
	case 417: // r: LONG_2FLOAT(r)
	case 414: // r: DOUBLE_SQRT(r)
	case 413: // r: FLOAT_SQRT(r)
	case 412: // r: DOUBLE_NEG(r)
	case 411: // r: FLOAT_NEG(r)
	case 370: // stm: RETURN(r)
	case 364: // stm: PREFETCH(r)
	case 357: // stm: INT_IFCMP(bittest,INT_CONSTANT)
	case 356: // stm: INT_IFCMP(szp,INT_CONSTANT)
	case 355: // stm: INT_IFCMP(cz,INT_CONSTANT)
	case 354: // stm: INT_IFCMP(boolcmp,INT_CONSTANT)
	case 353: // stm: INT_IFCMP(boolcmp,INT_CONSTANT)
	case 350: // stm: INT_IFCMP(sload16,INT_CONSTANT)
	case 347: // stm: INT_IFCMP(load8,INT_CONSTANT)
	case 346: // stm: INT_IFCMP(r,INT_CONSTANT)
	case 301: // r: PREPARE_INT(address,INT_CONSTANT)
	case 292: // r: INT_LOAD(address,INT_CONSTANT)
	case 285: // r: INT_2USHORT(load16_32)
	case 284: // r: INT_2USHORT(load16_32)
	case 283: // r: INT_AND(load16_32,INT_CONSTANT)
	case 277: // r: INT_2BYTE(load8_16_32)
	case 276: // r: INT_AND(load8_16_32,INT_CONSTANT)
	case 275: // uload8: INT_AND(load8_16_32,INT_CONSTANT)
	case 268: // r: LONG_MOVE(r)
	case 267: // load64: LONG_MOVE(load64)
	case 266: // load32: INT_MOVE(load32)
	case 265: // load16: INT_MOVE(load16)
	case 264: // uload16: INT_MOVE(uload16)
	case 263: // sload16: INT_MOVE(sload16)
	case 262: // load8: INT_MOVE(load8)
	case 261: // uload8: INT_MOVE(uload8)
	case 260: // sload8: INT_MOVE(sload8)
	case 259: // address: INT_MOVE(address)
	case 258: // address1scaledreg: INT_MOVE(address1scaledreg)
	case 257: // address1reg: INT_MOVE(address1reg)
	case 256: // szp: INT_MOVE(szp)
	case 255: // szpr: INT_MOVE(szpr)
	case 254: // cz: INT_MOVE(cz)
	case 253: // czr: INT_MOVE(czr)
	case 252: // r: INT_MOVE(riv)
	case 245: // r: LONG_2INT(load64)
	case 242: // r: LONG_2INT(r)
	case 241: // r: ADDR_2LONG(load32)
	case 240: // r: ADDR_2LONG(r)
	case 235: // r: INT_2LONG(load32)
	case 234: // r: INT_2LONG(r)
	case 231: // r: INT_2SHORT(load16_32)
	case 230: // r: INT_2SHORT(r)
	case 227: // r: INT_2USHORT(load16_32)
	case 226: // szpr: INT_2USHORT(r)
	case 223: // r: INT_2BYTE(load8_16_32)
	case 222: // r: INT_2BYTE(r)
	case 221: // r: LONG_NOT(r)
	case 211: // r: LONG_NEG(r)
	case 205: // r: INT_NOT(r)
	case 168: // szpr: INT_USHR(riv,INT_CONSTANT)
	case 161: // szpr: INT_SHR(riv,INT_CONSTANT)
	case 153: // r: INT_SHL(r,INT_CONSTANT)
	case 152: // szpr: INT_SHL(r,INT_CONSTANT)
	case 147: // szpr: INT_NEG(r)
	case 110: // r: CMP_CMOV(load32,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
	case 109: // r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
	case 108: // r: CMP_CMOV(load32,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
	case 107: // r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(INT_CONSTANT,INT_CONSTANT)))
	case 96: // boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
	case 95: // r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
	case 94: // boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
	case 93: // r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)
	case 92: // boolcmp: BOOLEAN_CMP_INT(bittest,INT_CONSTANT)
	case 91: // r: BOOLEAN_CMP_INT(bittest,INT_CONSTANT)
	case 90: // boolcmp: BOOLEAN_CMP_INT(szp,INT_CONSTANT)
	case 89: // r: BOOLEAN_CMP_INT(szp,INT_CONSTANT)
	case 88: // boolcmp: BOOLEAN_CMP_INT(cz,INT_CONSTANT)
	case 87: // r: BOOLEAN_CMP_INT(cz,INT_CONSTANT)
	case 86: // r: BOOLEAN_CMP_INT(load32,INT_CONSTANT)
	case 85: // r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
	case 84: // r: BOOLEAN_CMP_INT(load32,INT_CONSTANT)
	case 83: // r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
	case 82: // boolcmp: BOOLEAN_CMP_INT(r,INT_CONSTANT)
	case 81: // r: BOOLEAN_CMP_INT(r,INT_CONSTANT)
	case 76: // r: BOOLEAN_NOT(r)
	case 72: // stm: TRAP_IF(r,LONG_CONSTANT)
	case 71: // stm: TRAP_IF(r,INT_CONSTANT)
	case 69: // stm: SET_CAUGHT_EXCEPTION(r)
	case 66: // stm: NULL_CHECK(riv)
	case 61: // stm: LOWTABLESWITCH(r)
	case 54: // r: INT_MOVE(address)
	case 53: // r: INT_ADD(address,INT_CONSTANT)
	case 46: // address: INT_ADD(address1scaledreg,INT_CONSTANT)
	case 43: // address1scaledreg: INT_ADD(address1scaledreg,INT_CONSTANT)
	case 42: // address1reg: INT_ADD(address1reg,INT_CONSTANT)
	case 40: // address1reg: INT_MOVE(r)
	case 39: // address1reg: INT_ADD(r,INT_CONSTANT)
	case 38: // address1scaledreg: INT_SHL(r,INT_CONSTANT)
		mark(p.child1, ntsrule[0]);
		break;
	case 207: // stm: INT_ASTORE(INT_NOT(INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 206: // stm: INT_STORE(INT_NOT(INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 172: // stm: INT_ASTORE(INT_USHR(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 170: // stm: INT_STORE(INT_USHR(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 165: // stm: INT_ASTORE(INT_SHR(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 163: // stm: INT_STORE(INT_SHR(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 158: // stm: INT_ASTORE(INT_SHL(INT_ALOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 156: // stm: INT_STORE(INT_SHL(INT_LOAD(riv,riv),INT_CONSTANT),OTHER_OPERAND(riv,riv))
	case 149: // stm: INT_ASTORE(INT_NEG(INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 148: // stm: INT_STORE(INT_NEG(INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 78: // stm: BYTE_ASTORE(BOOLEAN_NOT(UBYTE_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 77: // stm: BYTE_STORE(BOOLEAN_NOT(UBYTE_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		mark(p.child1.child1.child1, ntsrule[0]);
		mark(p.child1.child1.child2, ntsrule[1]);
		mark(p.child2.child1, ntsrule[2]);
		mark(p.child2.child2, ntsrule[3]);
		break;
	case 484: // stm: LONG_STORE(load64,OTHER_OPERAND(riv,riv))
	case 483: // stm: LONG_ASTORE(load64,OTHER_OPERAND(riv,riv))
	case 474: // r: FCMP_FCMOV(r,OTHER_OPERAND(double_load,any))
	case 473: // r: FCMP_FCMOV(r,OTHER_OPERAND(float_load,any))
	case 468: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,any))
	case 467: // r: FCMP_CMOV(double_load,OTHER_OPERAND(r,any))
	case 466: // r: FCMP_CMOV(float_load,OTHER_OPERAND(r,any))
	case 465: // r: FCMP_CMOV(r,OTHER_OPERAND(double_load,any))
	case 464: // r: FCMP_CMOV(r,OTHER_OPERAND(float_load,any))
	case 463: // r: FCMP_CMOV(r,OTHER_OPERAND(r,any))
	case 432: // stm: FLOAT_ASTORE(r,OTHER_OPERAND(riv,riv))
	case 431: // stm: FLOAT_STORE(r,OTHER_OPERAND(riv,riv))
	case 430: // stm: DOUBLE_ASTORE(r,OTHER_OPERAND(riv,riv))
	case 429: // stm: DOUBLE_STORE(r,OTHER_OPERAND(riv,riv))
	case 343: // stm: LONG_ASTORE(r,OTHER_OPERAND(riv,riv))
	case 341: // stm: LONG_STORE(r,OTHER_OPERAND(riv,riv))
	case 340: // stm: INT_ASTORE(riv,OTHER_OPERAND(riv,riv))
	case 338: // stm: INT_STORE(riv,OTHER_OPERAND(address1reg,address1scaledreg))
	case 337: // stm: INT_STORE(riv,OTHER_OPERAND(address1scaledreg,address1reg))
	case 336: // stm: INT_STORE(riv,OTHER_OPERAND(address1scaledreg,riv))
	case 335: // stm: INT_STORE(riv,OTHER_OPERAND(riv,address1scaledreg))
	case 334: // stm: INT_STORE(riv,OTHER_OPERAND(riv,riv))
	case 333: // stm: SHORT_ASTORE(load16,OTHER_OPERAND(riv,riv))
	case 332: // stm: SHORT_ASTORE(riv,OTHER_OPERAND(riv,riv))
	case 331: // stm: SHORT_STORE(load16,OTHER_OPERAND(riv,riv))
	case 330: // stm: SHORT_STORE(riv,OTHER_OPERAND(riv,riv))
	case 329: // stm: BYTE_ASTORE(load8,OTHER_OPERAND(riv,riv))
	case 328: // stm: BYTE_ASTORE(riv,OTHER_OPERAND(riv,riv))
	case 327: // stm: BYTE_STORE(load8,OTHER_OPERAND(riv,riv))
	case 326: // stm: BYTE_STORE(riv,OTHER_OPERAND(riv,riv))
	case 122: // r: LCMP_CMOV(r,OTHER_OPERAND(rlv,any))
	case 116: // r: CMP_CMOV(riv,OTHER_OPERAND(load32,any))
	case 115: // r: CMP_CMOV(load32,OTHER_OPERAND(riv,any))
	case 113: // r: CMP_CMOV(riv,OTHER_OPERAND(uload8,any))
	case 112: // r: CMP_CMOV(uload8,OTHER_OPERAND(riv,any))
	case 105: // r: CMP_CMOV(r,OTHER_OPERAND(riv,any))
	case 102: // stm: BYTE_ASTORE(boolcmp,OTHER_OPERAND(riv,riv))
	case 101: // stm: BYTE_STORE(boolcmp,OTHER_OPERAND(riv,riv))
		mark(p.child1, ntsrule[0]);
		mark(p.child2.child1, ntsrule[1]);
		mark(p.child2.child2, ntsrule[2]);
		break;
	case 121: // r: CMP_CMOV(szp,OTHER_OPERAND(INT_CONSTANT,any))
	case 120: // r: CMP_CMOV(cz,OTHER_OPERAND(INT_CONSTANT,any))
	case 119: // r: CMP_CMOV(bittest,OTHER_OPERAND(INT_CONSTANT,any))
	case 118: // r: CMP_CMOV(boolcmp,OTHER_OPERAND(INT_CONSTANT,any))
	case 117: // r: CMP_CMOV(boolcmp,OTHER_OPERAND(INT_CONSTANT,any))
	case 114: // r: CMP_CMOV(sload16,OTHER_OPERAND(INT_CONSTANT,any))
	case 111: // r: CMP_CMOV(load8,OTHER_OPERAND(INT_CONSTANT,any))
	case 106: // r: CMP_CMOV(r,OTHER_OPERAND(INT_CONSTANT,any))
		mark(p.child1, ntsrule[0]);
		mark(p.child2.child2, ntsrule[1]);
		break;
	case 203: // stm: INT_ASTORE(INT_XOR(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 201: // stm: INT_STORE(INT_XOR(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 196: // stm: INT_ASTORE(INT_OR(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 194: // stm: INT_STORE(INT_OR(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 189: // stm: INT_ASTORE(INT_AND(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 187: // stm: INT_STORE(INT_AND(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 138: // stm: INT_ASTORE(INT_SUB(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 136: // stm: INT_STORE(INT_SUB(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 129: // stm: INT_ASTORE(INT_ADD(INT_ALOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
	case 127: // stm: INT_STORE(INT_ADD(INT_LOAD(riv,riv),riv),OTHER_OPERAND(riv,riv))
		mark(p.child1.child1.child1, ntsrule[0]);
		mark(p.child1.child1.child2, ntsrule[1]);
		mark(p.child1.child2, ntsrule[2]);
		mark(p.child2.child1, ntsrule[3]);
		mark(p.child2.child2, ntsrule[4]);
		break;
	case 204: // stm: INT_ASTORE(INT_XOR(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 202: // stm: INT_STORE(INT_XOR(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 197: // stm: INT_ASTORE(INT_OR(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 195: // stm: INT_STORE(INT_OR(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 190: // stm: INT_ASTORE(INT_AND(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 188: // stm: INT_STORE(INT_AND(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 139: // stm: INT_ASTORE(INT_SUB(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 137: // stm: INT_STORE(INT_SUB(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 130: // stm: INT_ASTORE(INT_ADD(riv,INT_ALOAD(riv,riv)),OTHER_OPERAND(riv,riv))
	case 128: // stm: INT_STORE(INT_ADD(riv,INT_LOAD(riv,riv)),OTHER_OPERAND(riv,riv))
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child1.child2.child1, ntsrule[1]);
		mark(p.child1.child2.child2, ntsrule[2]);
		mark(p.child2.child1, ntsrule[3]);
		mark(p.child2.child2, ntsrule[4]);
		break;
	case 339: // stm: INT_STORE(riv,OTHER_OPERAND(address,INT_CONSTANT))
	case 217: // r: LONG_USHR(rlv,INT_AND(riv,INT_CONSTANT))
	case 215: // r: LONG_SHR(rlv,INT_AND(riv,INT_CONSTANT))
	case 213: // r: LONG_SHL(rlv,INT_AND(riv,INT_CONSTANT))
	case 166: // szpr: INT_USHR(riv,INT_AND(r,INT_CONSTANT))
	case 159: // szpr: INT_SHR(riv,INT_AND(r,INT_CONSTANT))
	case 150: // szpr: INT_SHL(riv,INT_AND(r,INT_CONSTANT))
		mark(p.child1, ntsrule[0]);
		mark(p.child2.child1, ntsrule[1]);
		break;
	case 386: // bittest: INT_AND(INT_SHR(r,INT_CONSTANT),INT_CONSTANT)
	case 383: // bittest: INT_AND(INT_USHR(r,INT_CONSTANT),INT_CONSTANT)
	case 286: // r: INT_USHR(INT_SHL(load16_32,INT_CONSTANT),INT_CONSTANT)
	case 278: // r: INT_USHR(INT_SHL(load8_16_32,INT_CONSTANT),INT_CONSTANT)
	case 251: // load32: LONG_2INT(LONG_SHR(load64,INT_CONSTANT))
	case 250: // load32: LONG_2INT(LONG_USHR(load64,INT_CONSTANT))
	case 249: // r: LONG_2INT(LONG_SHR(load64,INT_CONSTANT))
	case 248: // r: LONG_2INT(LONG_USHR(load64,INT_CONSTANT))
	case 247: // r: LONG_2INT(LONG_SHR(r,INT_CONSTANT))
	case 246: // r: LONG_2INT(LONG_USHR(r,INT_CONSTANT))
	case 239: // r: LONG_SHL(INT_2LONG(load64),INT_CONSTANT)
	case 238: // r: LONG_SHL(INT_2LONG(r),INT_CONSTANT)
	case 237: // r: LONG_AND(INT_2LONG(load32),LONG_CONSTANT)
	case 236: // r: LONG_AND(INT_2LONG(r),LONG_CONSTANT)
	case 154: // szpr: INT_SHL(INT_SHR(r,INT_CONSTANT),INT_CONSTANT)
		mark(p.child1.child1, ntsrule[0]);
		break;
	case 171: // stm: INT_ASTORE(INT_USHR(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 169: // stm: INT_STORE(INT_USHR(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 164: // stm: INT_ASTORE(INT_SHR(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 162: // stm: INT_STORE(INT_SHR(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 157: // stm: INT_ASTORE(INT_SHL(INT_ALOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
	case 155: // stm: INT_STORE(INT_SHL(INT_LOAD(riv,riv),INT_AND(r,INT_CONSTANT)),OTHER_OPERAND(riv,riv))
		mark(p.child1.child1.child1, ntsrule[0]);
		mark(p.child1.child1.child2, ntsrule[1]);
		mark(p.child1.child2.child1, ntsrule[2]);
		mark(p.child2.child1, ntsrule[3]);
		mark(p.child2.child2, ntsrule[4]);
		break;
	case 176: // r: INT_OR(INT_USHR(r,INT_CONSTANT),INT_SHL(r,INT_CONSTANT))
	case 175: // r: INT_OR(INT_SHL(r,INT_CONSTANT),INT_USHR(r,INT_CONSTANT))
	case 174: // r: INT_OR(INT_USHR(r,INT_CONSTANT),INT_SHL(r,INT_CONSTANT))
	case 173: // r: INT_OR(INT_SHL(r,INT_CONSTANT),INT_USHR(r,INT_CONSTANT))
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child2.child1, ntsrule[1]);
		break;
	case 180: // r: INT_OR(INT_USHR(r,INT_AND(r,INT_CONSTANT)),INT_SHL(r,INT_AND(INT_NEG(r),INT_CONSTANT)))
	case 177: // r: INT_OR(INT_SHL(r,INT_AND(r,INT_CONSTANT)),INT_USHR(r,INT_AND(INT_NEG(r),INT_CONSTANT)))
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child1.child2.child1, ntsrule[1]);
		mark(p.child2.child1, ntsrule[2]);
		mark(p.child2.child2.child1.child1, ntsrule[3]);
		break;
	case 179: // r: INT_OR(INT_SHL(r,INT_AND(INT_NEG(r),INT_CONSTANT)),INT_USHR(r,INT_AND(r,INT_CONSTANT)))
	case 178: // r: INT_OR(INT_USHR(r,INT_AND(INT_NEG(r),INT_CONSTANT)),INT_SHL(r,INT_AND(r,INT_CONSTANT)))
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child1.child2.child1.child1, ntsrule[1]);
		mark(p.child2.child1, ntsrule[2]);
		mark(p.child2.child2.child1, ntsrule[3]);
		break;
	case 244: // stm: INT_ASTORE(LONG_2INT(r),OTHER_OPERAND(riv,riv))
	case 243: // stm: INT_STORE(LONG_2INT(r),OTHER_OPERAND(riv,riv))
	case 233: // stm: SHORT_ASTORE(INT_2SHORT(r),OTHER_OPERAND(riv,riv))
	case 232: // stm: SHORT_STORE(INT_2SHORT(r),OTHER_OPERAND(riv,riv))
	case 229: // stm: SHORT_ASTORE(INT_2USHORT(r),OTHER_OPERAND(riv,riv))
	case 228: // stm: SHORT_STORE(INT_2USHORT(r),OTHER_OPERAND(riv,riv))
	case 225: // stm: BYTE_ASTORE(INT_2BYTE(r),OTHER_OPERAND(riv,riv))
	case 224: // stm: BYTE_STORE(INT_2BYTE(r),OTHER_OPERAND(riv,riv))
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child2.child1, ntsrule[1]);
		mark(p.child2.child2, ntsrule[2]);
		break;
	case 378: // r: SYSCALL(INT_CONSTANT,any)
	case 375: // r: CALL(INT_CONSTANT,any)
	case 373: // r: CALL(BRANCH_TARGET,any)
	case 302: // r: PREPARE_INT(INT_CONSTANT,address)
		mark(p.child2, ntsrule[0]);
		break;
	case 472: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(double_load,r)))
	case 471: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(float_load,r)))
	case 470: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(r,double_load)))
	case 469: // r: FCMP_FCMOV(r,OTHER_OPERAND(r,OTHER_OPERAND(r,float_load)))
	case 325: // r: ATTEMPT_LONG(riv,OTHER_OPERAND(riv,OTHER_OPERAND(rlv,rlv)))
	case 308: // r: ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv)))
	case 307: // r: ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv)))
	case 306: // r: ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv)))
	case 305: // r: ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv)))
	case 304: // r: ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv)))
		mark(p.child1, ntsrule[0]);
		mark(p.child2.child1, ntsrule[1]);
		mark(p.child2.child2.child1, ntsrule[2]);
		mark(p.child2.child2.child2, ntsrule[3]);
		break;
	case 309: // r: ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv)))
		mark(p.child1, ntsrule[0]);
		mark(p.child2.child2.child1, ntsrule[1]);
		mark(p.child2.child2.child2, ntsrule[2]);
		break;
	case 310: // r: ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv)))
		mark(p.child2.child1, ntsrule[0]);
		mark(p.child2.child2.child1, ntsrule[1]);
		mark(p.child2.child2.child2, ntsrule[2]);
		break;
	case 322: // stm: INT_IFCMP(ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 321: // stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 320: // stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 319: // stm: INT_IFCMP(ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 318: // stm: INT_IFCMP(ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 315: // stm: INT_IFCMP(ATTEMPT_INT(address1reg,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 314: // stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(address1reg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 313: // stm: INT_IFCMP(ATTEMPT_INT(address1scaledreg,OTHER_OPERAND(r,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 312: // stm: INT_IFCMP(ATTEMPT_INT(r,OTHER_OPERAND(address1scaledreg,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 311: // stm: INT_IFCMP(ATTEMPT_INT(riv,OTHER_OPERAND(riv,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child1.child2.child1, ntsrule[1]);
		mark(p.child1.child2.child2.child1, ntsrule[2]);
		mark(p.child1.child2.child2.child2, ntsrule[3]);
		break;
	case 323: // stm: INT_IFCMP(ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 316: // stm: INT_IFCMP(ATTEMPT_INT(address,OTHER_OPERAND(INT_CONSTANT,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child1.child2.child2.child1, ntsrule[1]);
		mark(p.child1.child2.child2.child2, ntsrule[2]);
		break;
	case 324: // stm: INT_IFCMP(ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
	case 317: // stm: INT_IFCMP(ATTEMPT_INT(INT_CONSTANT,OTHER_OPERAND(address,OTHER_OPERAND(riv,riv))),INT_CONSTANT)
		mark(p.child1.child2.child1, ntsrule[0]);
		mark(p.child1.child2.child2.child1, ntsrule[1]);
		mark(p.child1.child2.child2.child2, ntsrule[2]);
		break;
	case 344: // stm: LONG_ASTORE(LONG_CONSTANT,OTHER_OPERAND(riv,riv))
	case 342: // stm: LONG_STORE(LONG_CONSTANT,OTHER_OPERAND(riv,riv))
		mark(p.child2.child1, ntsrule[0]);
		mark(p.child2.child2, ntsrule[1]);
		break;
	case 377: // r: SYSCALL(INT_LOAD(riv,riv),any)
	case 374: // r: CALL(INT_LOAD(riv,riv),any)
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child1.child2, ntsrule[1]);
		mark(p.child2, ntsrule[2]);
		break;
	case 385: // bittest: INT_AND(INT_SHR(load32,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
	case 384: // bittest: INT_AND(INT_SHR(r,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
	case 382: // bittest: INT_AND(INT_USHR(load32,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
	case 381: // bittest: INT_AND(INT_USHR(r,INT_AND(r,INT_CONSTANT)),INT_CONSTANT)
		mark(p.child1.child1, ntsrule[0]);
		mark(p.child1.child2.child1, ntsrule[1]);
		break;
	case 388: // bittest: INT_AND(INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)),load32)
	case 387: // bittest: INT_AND(INT_SHL(INT_CONSTANT,INT_AND(riv,INT_CONSTANT)),r)
		mark(p.child1.child2.child1, ntsrule[0]);
		mark(p.child2, ntsrule[1]);
		break;
	case 390: // bittest: INT_AND(load32,INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)))
	case 389: // bittest: INT_AND(r,INT_SHL(INT_CONSTANT,INT_AND(r,INT_CONSTANT)))
		mark(p.child1, ntsrule[0]);
		mark(p.child2.child2.child1, ntsrule[1]);
		break;
	case 479: // r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,DOUBLE_NEG(r))))
	case 475: // r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,FLOAT_NEG(r))))
		mark(p.child1, ntsrule[0]);
		mark(p.child2.child2.child1, ntsrule[1]);
		mark(p.child2.child2.child2.child1, ntsrule[2]);
		break;
	case 480: // r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(DOUBLE_NEG(r),r)))
	case 476: // r: FCMP_FCMOV(r,OTHER_OPERAND(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(FLOAT_NEG(r),r)))
		mark(p.child1, ntsrule[0]);
		mark(p.child2.child2.child1.child1, ntsrule[1]);
		mark(p.child2.child2.child2, ntsrule[2]);
		break;
	case 481: // r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(DOUBLE_NEG(r),r)))
	case 477: // r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(FLOAT_NEG(r),r)))
		mark(p.child2.child1, ntsrule[0]);
		mark(p.child2.child2.child1.child1, ntsrule[1]);
		mark(p.child2.child2.child2, ntsrule[2]);
		break;
	case 482: // r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(r,DOUBLE_NEG(r))))
	case 478: // r: FCMP_FCMOV(MATERIALIZE_FP_CONSTANT(INT_CONSTANT),OTHER_OPERAND(r,OTHER_OPERAND(r,FLOAT_NEG(r))))
		mark(p.child2.child1, ntsrule[0]);
		mark(p.child2.child2.child1, ntsrule[1]);
		mark(p.child2.child2.child2.child1, ntsrule[2]);
		break;
	}
}

public static final byte[] action={0
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION | RIGHT_CHILD_FIRST
   ,EMIT_INSTRUCTION | RIGHT_CHILD_FIRST
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION | RIGHT_CHILD_FIRST
   ,EMIT_INSTRUCTION | RIGHT_CHILD_FIRST
   ,EMIT_INSTRUCTION | RIGHT_CHILD_FIRST
   ,EMIT_INSTRUCTION | RIGHT_CHILD_FIRST
   ,EMIT_INSTRUCTION | RIGHT_CHILD_FIRST
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,NOFLAGS
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
   ,EMIT_INSTRUCTION
};

void code16(BURS_TreeNode p) {
    pushMO(MO_L(P(p), B));
}
void code17(BURS_TreeNode p) {
    pushMO(MO_AL(P(p), B_S, B));
}
void code18(BURS_TreeNode p) {
    pushMO(MO_L(P(p), B));
}
void code19(BURS_TreeNode p) {
    pushMO(MO_AL(P(p), B_S, B));
}
void code22(BURS_TreeNode p) {
    pushMO(MO_L(P(p), W));
}
void code23(BURS_TreeNode p) {
    pushMO(MO_AL(P(p), W_S, W));
}
void code24(BURS_TreeNode p) {
    pushMO(MO_L(P(p), W));
}
void code25(BURS_TreeNode p) {
    pushMO(MO_AL(P(p), W_S, W));
}
void code28(BURS_TreeNode p) {
    pushMO(MO_L(P(p), DW));
}
void code29(BURS_TreeNode p) {
    pushMO(MO_AL(P(p), DW_S, DW));
}
void code34(BURS_TreeNode p) {
    pushMO(MO_L(P(p), QW));
}
void code35(BURS_TreeNode p) {
    pushMO(MO_AL(P(p), QW_S, QW));
}
void code38(BURS_TreeNode p) {
    pushAddress(null, Binary.getVal1(P(p)).asRegister(), LEA_SHIFT(Binary.getVal2(P(p))), Offset.zero());
}
void code39(BURS_TreeNode p) {
    pushAddress(R(Binary.getVal1(P(p))), null, B_S, Offset.fromIntSignExtend(VR(p)));
}
void code40(BURS_TreeNode p) {
    pushAddress(R(Move.getVal(P(p))), null, B_S, Offset.zero());
}
void code41(BURS_TreeNode p) {
    pushAddress(R(Binary.getVal1(P(p))), R(Binary.getVal2(P(p))), B_S, Offset.zero());
}
void code42(BURS_TreeNode p) {
    augmentAddress(Binary.getVal2(P(p)));
}
void code43(BURS_TreeNode p) {
    augmentAddress(Binary.getVal2(P(p)));
}
void code44(BURS_TreeNode p) {
    augmentAddress(Binary.getVal1(P(p)));
}
void code45(BURS_TreeNode p) {
    augmentAddress(Binary.getVal2(P(p)));
}
void code46(BURS_TreeNode p) {
    augmentAddress(Binary.getVal2(P(p)));
}
void code47(BURS_TreeNode p) {
    combineAddresses();
}
void code48(BURS_TreeNode p) {
    combineAddresses();
}
void code49(BURS_TreeNode p) {
    augmentAddress(Binary.getVal2(P(p))); 
EMIT_Lea(P(p), Binary.getResult(P(p)), consumeAddress(DW, null, null));
}
void code50(BURS_TreeNode p) {
    augmentAddress(Binary.getVal1(P(p))); 
EMIT_Lea(P(p), Binary.getResult(P(p)), consumeAddress(DW, null, null));
}
void code51(BURS_TreeNode p) {
    combineAddresses(); 
EMIT_Lea(P(p), Binary.getResult(P(p)), consumeAddress(DW, null, null));
}
void code52(BURS_TreeNode p) {
    combineAddresses(); 
EMIT_Lea(P(p), Binary.getResult(P(p)), consumeAddress(DW, null, null));
}
void code53(BURS_TreeNode p) {
    augmentAddress(Binary.getVal2(P(p))); 
EMIT_Lea(P(p), Binary.getResult(P(p)), consumeAddress(DW, null, null));
}
void code54(BURS_TreeNode p) {
    EMIT_Lea(P(p), Move.getResult(P(p)), consumeAddress(DW, null, null));
}
void code55(BURS_TreeNode p) {
    EMIT(InlineGuard.mutate(P(p), IG_PATCH_POINT, null, null, null, InlineGuard.getTarget(P(p)), InlineGuard.getBranchProfile(P(p))));
}
void code56(BURS_TreeNode p) {
    EMIT(P(p));
}
void code57(BURS_TreeNode p) {
    EMIT(P(p));
}
void code58(BURS_TreeNode p) {
    EMIT(P(p));
}
void code59(BURS_TreeNode p) {
    EMIT(P(p));
}
void code60(BURS_TreeNode p) {
    EMIT(P(p));
}
void code61(BURS_TreeNode p) {
    LOWTABLESWITCH(P(p));
}
void code62(BURS_TreeNode p) {
    RESOLVE(P(p));
}
void code64(BURS_TreeNode p) {
    EMIT(P(p));
}
void code65(BURS_TreeNode p) {
    EMIT(P(p));
}
void code66(BURS_TreeNode p) {
    EMIT(P(p));
}
void code67(BURS_TreeNode p) {
    PROLOGUE(P(p));
}
void code68(BURS_TreeNode p) {
    GET_EXCEPTION_OBJECT(P(p));
}
void code69(BURS_TreeNode p) {
    SET_EXCEPTION_OBJECT(P(p));
}
void code70(BURS_TreeNode p) {
    EMIT(MIR_Trap.mutate(P(p), IA32_INT, Trap.getGuardResult(P(p)), Trap.getTCode(P(p))));
}
void code71(BURS_TreeNode p) {
    TRAP_IF_IMM(P(p), false);
}
void code72(BURS_TreeNode p) {
    TRAP_IF_IMM(P(p), true);
}
void code73(BURS_TreeNode p) {
    EMIT(MIR_TrapIf.mutate(P(p), IA32_TRAPIF, 
                       TrapIf.getGuardResult(P(p)), 
		       TrapIf.getVal1(P(p)), 
		       TrapIf.getVal2(P(p)), 
		       COND(TrapIf.getCond(P(p))), 
		       TrapIf.getTCode(P(p))));
}
void code74(BURS_TreeNode p) {
    EMIT(MIR_TrapIf.mutate(P(p), IA32_TRAPIF, 
                       TrapIf.getGuardResult(P(p)), 
		       consumeMO(), 
		       TrapIf.getVal2(P(p)), 
		       COND(TrapIf.getCond(P(p))), 
		       TrapIf.getTCode(P(p))));
}
void code75(BURS_TreeNode p) {
    EMIT(MIR_TrapIf.mutate(P(p), IA32_TRAPIF, 
                       TrapIf.getGuardResult(P(p)), 
		       TrapIf.getVal1(P(p)), 
	               consumeMO(), 
		       COND(TrapIf.getCond(P(p))), 
		       TrapIf.getTCode(P(p))));
}
void code76(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)), IC(1));
}
void code77(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), MO_S(P(p), B), MO_S(P(p), B), IC(1));
}
void code78(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), MO_AS(P(p), B_S, B), MO_AS(P(p), B_S, B), IC(1));
}
void code79(BURS_TreeNode p) {
    BOOLEAN_CMP_INT(P(p), BooleanCmp.getResult(P(p)), 
   BooleanCmp.getVal1(P(p)), BooleanCmp.getVal2(P(p)), 
   BooleanCmp.getCond(P(p)));
}
void code80(BURS_TreeNode p) {
    pushCOND(BooleanCmp.getCond(P(p))); 
EMIT(MIR_Compare.mutate(P(p), IA32_CMP, BooleanCmp.getVal1(P(p)), BooleanCmp.getVal2(P(p))));
}
void code81(BURS_TreeNode p) {
    EMIT(CPOS(P(p),MIR_Test.create(IA32_TEST, BooleanCmp.getVal1(P(p)), BooleanCmp.getVal1(P(p)).copy())));
BOOLEAN_CMP_INT(P(p), BooleanCmp.getResult(P(p)), BooleanCmp.getCond(P(p)));
}
void code82(BURS_TreeNode p) {
    pushCOND(BooleanCmp.getCond(P(p))); 
EMIT(CPOS(P(p),MIR_Test.create(IA32_TEST, BooleanCmp.getVal1(P(p)), BooleanCmp.getVal1(P(p)).copy())));
}
void code83(BURS_TreeNode p) {
    EMIT_Commutative(IA32_SHR, P(p), BooleanCmp.getResult(P(p)), BooleanCmp.getVal1(P(p)), IC(31));
}
void code84(BURS_TreeNode p) {
    EMIT_Commutative(IA32_SHR, P(p), BooleanCmp.getResult(P(p)), consumeMO(), IC(31));
}
void code85(BURS_TreeNode p) {
    RegisterOperand result = BooleanCmp.getResult(P(p)); 
EMIT_Commutative(IA32_SHR, P(p), result, BooleanCmp.getVal1(P(p)), IC(31)); 
EMIT(CPOS(P(p),MIR_BinaryAcc.create(IA32_XOR, result.copyRO(), IC(1))));
}
void code86(BURS_TreeNode p) {
    RegisterOperand result = BooleanCmp.getResult(P(p)); 
EMIT_Commutative(IA32_SHR, P(p), result, consumeMO(), IC(31)); 
EMIT(CPOS(P(p),MIR_BinaryAcc.create(IA32_XOR, result.copyRO(), IC(1))));
}
void code87(BURS_TreeNode p) {
    BOOLEAN_CMP_INT(P(p), BooleanCmp.getResult(P(p)), BooleanCmp.getCond(P(p)));
}
void code88(BURS_TreeNode p) {
    pushCOND(BooleanCmp.getCond(P(p)));
}
void code89(BURS_TreeNode p) {
    BOOLEAN_CMP_INT(P(p), BooleanCmp.getResult(P(p)), BooleanCmp.getCond(P(p)));
}
void code90(BURS_TreeNode p) {
    pushCOND(BooleanCmp.getCond(P(p)));
}
void code91(BURS_TreeNode p) {
    BOOLEAN_CMP_INT(P(p), BooleanCmp.getResult(P(p)), BIT_TEST(VR(p),BooleanCmp.getCond(P(p))));
}
void code92(BURS_TreeNode p) {
    pushCOND(BIT_TEST(VR(p),BooleanCmp.getCond(P(p))));
}
void code93(BURS_TreeNode p) {
    BOOLEAN_CMP_INT(P(p), BooleanCmp.getResult(P(p)), consumeCOND());
}
void code95(BURS_TreeNode p) {
    BOOLEAN_CMP_INT(P(p), BooleanCmp.getResult(P(p)), consumeCOND().flipCode());
}
void code96(BURS_TreeNode p) {
    pushCOND(consumeCOND().flipCode()); // invert already pushed condition
}
void code97(BURS_TreeNode p) {
    BOOLEAN_CMP_INT(PL(p), BooleanCmp.getResult(P(p)), 
            consumeMO(), BooleanCmp.getVal2(P(p)), 
	    BooleanCmp.getCond(P(p)));
}
void code98(BURS_TreeNode p) {
    pushCOND(BooleanCmp.getCond(P(p))); 
EMIT(MIR_Compare.mutate(PL(p), IA32_CMP, consumeMO(), BooleanCmp.getVal2(P(p))));
}
void code99(BURS_TreeNode p) {
    BOOLEAN_CMP_INT(PR(p), BooleanCmp.getResult(P(p)), 
            BooleanCmp.getVal1(P(p)), consumeMO(), 
	    BooleanCmp.getCond(P(p)));
}
void code100(BURS_TreeNode p) {
    pushCOND(BooleanCmp.getCond(P(p)).flipOperands()); 
EMIT(MIR_Compare.mutate(PR(p), IA32_CMP, consumeMO(), BooleanCmp.getVal1(P(p))));
}
void code101(BURS_TreeNode p) {
    EMIT(MIR_Set.mutate(P(p), IA32_SET__B, MO_S(P(p),B), COND(consumeCOND())));
}
void code102(BURS_TreeNode p) {
    EMIT(MIR_Set.mutate(P(p), IA32_SET__B, MO_AS(P(p),B_S,B), COND(consumeCOND())));
}
void code103(BURS_TreeNode p) {
    BOOLEAN_CMP_LONG(P(p), BooleanCmp.getResult(P(p)), BooleanCmp.getVal1(P(p)), BooleanCmp.getVal2(P(p)), BooleanCmp.getCond(P(p)));
}
void code104(BURS_TreeNode p) {
    pushCOND(BooleanCmp.getCond(P(p))); 
LONG_CMP(P(p), BooleanCmp.getResult(P(p)), BooleanCmp.getVal1(P(p)), BooleanCmp.getVal2(P(p))); 
EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, BooleanCmp.getResult(P(p)), IC(0))));
}
void code105(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP,  CondMove.getVal1(P(p)), CondMove.getVal2(P(p))))); 
CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code106(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Test.create(IA32_TEST, CondMove.getVal1(P(p)), CondMove.getVal1(P(p)).copy()))); 
CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code107(BURS_TreeNode p) {
    EMIT_Commutative(IA32_SAR, P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), IC(31));
}
void code108(BURS_TreeNode p) {
    EMIT_Commutative(IA32_SAR, P(p), CondMove.getResult(P(p)), consumeMO(), IC(31));
}
void code109(BURS_TreeNode p) {
    RegisterOperand result = CondMove.getResult(P(p)); 
EMIT_Commutative(IA32_SAR, P(p), result, CondMove.getVal1(P(p)), IC(31)); 
EMIT(CPOS(P(p),MIR_UnaryAcc.create(IA32_NOT, result.copyRO())));
}
void code110(BURS_TreeNode p) {
    RegisterOperand result = CondMove.getResult(P(p)); 
EMIT_Commutative(IA32_SAR, P(p), result, consumeMO(), IC(31)); 
EMIT(CPOS(P(p),MIR_UnaryAcc.create(IA32_NOT, result.copyRO())));
}
void code111(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, consumeMO(), CondMove.getVal2(P(p))))); 
CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code112(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, consumeMO(), CondMove.getVal2(P(p))))); 
CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code113(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, consumeMO(), CondMove.getVal2(P(p))))); 
CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code114(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, consumeMO(), CondMove.getVal2(P(p))))); 
CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code115(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, consumeMO(), CondMove.getVal2(P(p))))); 
CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code116(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, consumeMO(), CondMove.getVal1(P(p))))); 
CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)).flipOperands(), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code117(BURS_TreeNode p) {
    CMOV_MOV(P(p), CondMove.getResult(P(p)), consumeCOND(), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code118(BURS_TreeNode p) {
    CMOV_MOV(P(p), CondMove.getResult(P(p)), consumeCOND().flipCode(), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code119(BURS_TreeNode p) {
    CMOV_MOV(P(p), CondMove.getResult(P(p)), BIT_TEST(VRL(p), CondMove.getCond(P(p))), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code120(BURS_TreeNode p) {
    CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code121(BURS_TreeNode p) {
    CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)), 
         CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code122(BURS_TreeNode p) {
    LCMP_CMOV(P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), CondMove.getVal2(P(p)),
          CondMove.getCond(P(p)), CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code123(BURS_TreeNode p) {
    EMIT_Commutative(IA32_ADD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code124(BURS_TreeNode p) {
    if (Binary.getVal2(P(p)).isIntConstant()) { 
 pushAddress(R(Binary.getVal1(P(p))), null, B_S, Offset.fromIntSignExtend(VR(p))); 
} else { 
 pushAddress(R(Binary.getVal1(P(p))), R(Binary.getVal2(P(p))), B_S, Offset.zero()); 
} 
EMIT_Lea(P(p), Binary.getResult(P(p)), consumeAddress(DW, null, null));
}
void code125(BURS_TreeNode p) {
    EMIT_Commutative(IA32_ADD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code126(BURS_TreeNode p) {
    EMIT_Commutative(IA32_ADD, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO());
}
void code127(BURS_TreeNode p) {
    EMIT_Commutative(IA32_ADD, P(p), MO_S(P(p), DW), MO_S(P(p), DW), Binary.getVal2(PL(p)));
}
void code128(BURS_TreeNode p) {
    EMIT_Commutative(IA32_ADD, P(p), MO_S(P(p), DW), MO_S(P(p), DW), Binary.getVal1(PL(p)));
}
void code129(BURS_TreeNode p) {
    EMIT_Commutative(IA32_ADD, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW), Binary.getVal2(PL(p)));
}
void code130(BURS_TreeNode p) {
    EMIT_Commutative(IA32_ADD, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW), Binary.getVal1(PL(p)));
}
void code131(BURS_TreeNode p) {
    EMIT_NonCommutative(IA32_SUB, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code132(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_UnaryAcc.create(IA32_NEG, Binary.getResult(P(p))))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_ADD, Binary.getResult(P(p)), Binary.getVal1(P(p))));
}
void code133(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_UnaryAcc.create(IA32_NEG, Binary.getResult(P(p))))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_ADD, Binary.getResult(P(p)), consumeMO()));
}
void code134(BURS_TreeNode p) {
    EMIT_NonCommutative(IA32_SUB, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code135(BURS_TreeNode p) {
    EMIT_NonCommutative(IA32_SUB, P(p), Binary.getResult(P(p)), consumeMO(), Binary.getVal2(P(p)));
}
void code136(BURS_TreeNode p) {
    EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SUB, MO_S(P(p), DW), Binary.getVal2(PL(p))));
}
void code137(BURS_TreeNode p) {
    MemoryOperand result = MO_S(P(p), DW); 
EMIT(CPOS(P(p), MIR_UnaryAcc.create(IA32_NEG, result))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_ADD, result.copy(), Binary.getVal1(PL(p))));
}
void code138(BURS_TreeNode p) {
    EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SUB, MO_AS(P(p), DW_S, DW), Binary.getVal2(PL(p))));
}
void code139(BURS_TreeNode p) {
    MemoryOperand result = MO_AS(P(p), DW_S, DW); 
EMIT(CPOS(P(p), MIR_UnaryAcc.create(IA32_NEG, result))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_ADD, result.copy(), Binary.getVal1(PL(p))));
}
void code140(BURS_TreeNode p) {
    EMIT_Commutative(IA32_IMUL2, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code141(BURS_TreeNode p) {
    EMIT_Commutative(IA32_IMUL2, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code142(BURS_TreeNode p) {
    EMIT_Commutative(IA32_IMUL2, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO());
}
void code143(BURS_TreeNode p) {
    INT_DIVIDES(P(p), GuardedBinary.getResult(P(p)), GuardedBinary.getVal1(P(p)), 
	                GuardedBinary.getVal2(P(p)), true);
}
void code144(BURS_TreeNode p) {
    INT_DIVIDES(P(p), GuardedBinary.getResult(P(p)), GuardedBinary.getVal1(P(p)), 
	                consumeMO(), true);
}
void code145(BURS_TreeNode p) {
    INT_DIVIDES(P(p), GuardedBinary.getResult(P(p)), GuardedBinary.getVal1(P(p)), 
	                GuardedBinary.getVal2(P(p)), false);
}
void code146(BURS_TreeNode p) {
    INT_DIVIDES(P(p), GuardedBinary.getResult(P(p)), GuardedBinary.getVal1(P(p)), 
	                consumeMO(), false);
}
void code147(BURS_TreeNode p) {
    EMIT_Unary(IA32_NEG, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code148(BURS_TreeNode p) {
    EMIT_Unary(IA32_NEG, P(p), MO_S(P(p), DW), MO_S(P(p), DW));
}
void code149(BURS_TreeNode p) {
    EMIT_Unary(IA32_NEG, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW));
}
void code150(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PR(p))))); 
EMIT_NonCommutative(IA32_SHL, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), new RegisterOperand(getECX(), TypeReference.Int));
}
void code151(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal2(P(p))))); 
EMIT_NonCommutative(IA32_SHL, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), new RegisterOperand(getECX(), TypeReference.Int));
}
void code152(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VR(p) & 0x7FFFFFFF) <= 31); if(Binary.getVal2(P(p)).asIntConstant().value == 1) { 
 EMIT_NonCommutative(IA32_ADD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal1(P(p)).copy()); 
} else { 
 EMIT_NonCommutative(IA32_SHL, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p))); 
}
}
void code153(BURS_TreeNode p) {
    pushAddress(null, Binary.getVal1(P(p)).asRegister(), LEA_SHIFT(Binary.getVal2(P(p))), Offset.zero()); 
EMIT_Lea(P(p), Binary.getResult(P(p)), consumeAddress(DW, null, null));
}
void code154(BURS_TreeNode p) {
    EMIT_Commutative(IA32_AND, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)), IC(0xffffffff << VR(p)));
}
void code155(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PLR(p))))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SHL, MO_S(P(p), DW), new RegisterOperand(getECX(), TypeReference.Int)));
}
void code156(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VLR(p) & 0x7FFFFFFF) <= 31); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SHL, MO_S(P(p), DW), Binary.getVal2(PL(p))));
}
void code157(BURS_TreeNode p) {
    EMIT(MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PLR(p)))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SHL, MO_AS(P(p), DW_S, DW), new RegisterOperand(getECX(), TypeReference.Int)));
}
void code158(BURS_TreeNode p) {
    EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SHL, MO_AS(P(p), DW_S, DW), Binary.getVal2(PL(p))));
}
void code159(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PR(p))))); 
EMIT_NonCommutative(IA32_SAR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), new RegisterOperand(getECX(), TypeReference.Int));
}
void code160(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal2(P(p))))); 
EMIT_NonCommutative(IA32_SAR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), new RegisterOperand(getECX(), TypeReference.Int));
}
void code161(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VR(p) & 0x7FFFFFFF) <= 31); 
EMIT_NonCommutative(IA32_SAR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code162(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PLR(p))))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SAR, MO_S(P(p), DW), new RegisterOperand(getECX(), TypeReference.Int)));
}
void code163(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VLR(p) & 0x7FFFFFFF) <= 31); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SAR, MO_S(P(p), DW), Binary.getVal2(PL(p))));
}
void code164(BURS_TreeNode p) {
    EMIT(MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PLR(p)))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SAR, MO_AS(P(p), DW_S, DW), new RegisterOperand(getECX(), TypeReference.Int)));
}
void code165(BURS_TreeNode p) {
    EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SAR, MO_AS(P(p), DW_S, DW), Binary.getVal2(PL(p))));
}
void code166(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PR(p))))); 
EMIT_NonCommutative(IA32_SHR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), new RegisterOperand(getECX(), TypeReference.Int));
}
void code167(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal2(P(p))))); 
EMIT_NonCommutative(IA32_SHR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), new RegisterOperand(getECX(), TypeReference.Int));
}
void code168(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VR(p) & 0x7FFFFFFF) <= 31); 
EMIT_NonCommutative(IA32_SHR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code169(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PLR(p))))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SHR, MO_S(P(p), DW), new RegisterOperand(getECX(), TypeReference.Int)));
}
void code170(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VLR(p) & 0x7FFFFFFF) <= 31); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SHR, MO_S(P(p), DW), Binary.getVal2(PL(p))));
}
void code171(BURS_TreeNode p) {
    EMIT(MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PLR(p)))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SHR, MO_AS(P(p), DW_S, DW), new RegisterOperand(getECX(), TypeReference.Int)));
}
void code172(BURS_TreeNode p) {
    EMIT(MIR_BinaryAcc.mutate(P(p), IA32_SHR, MO_AS(P(p), DW_S, DW), Binary.getVal2(PL(p))));
}
void code173(BURS_TreeNode p) {
    EMIT_NonCommutative(IA32_ROL, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)).copy(), IC(VLR(p)&0x1f));
}
void code174(BURS_TreeNode p) {
    EMIT_NonCommutative(IA32_ROL, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)).copy(), IC(VRR(p)&0x1f));
}
void code175(BURS_TreeNode p) {
    EMIT_NonCommutative(IA32_ROR, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)).copy(), IC(1));
}
void code176(BURS_TreeNode p) {
    EMIT_NonCommutative(IA32_ROR, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)).copy(), IC(1));
}
void code177(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PLR(p))))); 
EMIT_NonCommutative(IA32_ROL, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)).copy(), new RegisterOperand(getECX(), TypeReference.Int));
}
void code178(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PRR(p))))); 
EMIT_NonCommutative(IA32_ROL, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)).copy(), new RegisterOperand(getECX(), TypeReference.Int));
}
void code179(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PRR(p))))); 
EMIT_NonCommutative(IA32_ROR, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)).copy(), new RegisterOperand(getECX(), TypeReference.Int));
}
void code180(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(getECX(), TypeReference.Int), Binary.getVal1(PLR(p))))); 
EMIT_NonCommutative(IA32_ROR, P(p), Binary.getResult(P(p)), Binary.getVal1(PL(p)).copy(), new RegisterOperand(getECX(), TypeReference.Int));
}
void code181(BURS_TreeNode p) {
    EMIT_Commutative(IA32_AND, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code182(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_TEST, Binary.getVal1(P(p)), Binary.getVal2(P(p))));
}
void code183(BURS_TreeNode p) {
    EMIT_Commutative(IA32_AND, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code184(BURS_TreeNode p) {
    EMIT_Commutative(IA32_AND, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO());
}
void code185(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_TEST, consumeMO(), Binary.getVal2(P(p))));
}
void code186(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_TEST, consumeMO(), Binary.getVal1(P(p))));
}
void code187(BURS_TreeNode p) {
    EMIT_Commutative(IA32_AND, P(p), MO_S(P(p), DW), MO_S(P(p), DW), Binary.getVal2(PL(p)) );
}
void code188(BURS_TreeNode p) {
    EMIT_Commutative(IA32_AND, P(p), MO_S(P(p), DW), MO_S(P(p), DW), Binary.getVal1(PL(p)) );
}
void code189(BURS_TreeNode p) {
    EMIT_Commutative(IA32_AND, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW), Binary.getVal2(PL(p)) );
}
void code190(BURS_TreeNode p) {
    EMIT_Commutative(IA32_AND, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW), Binary.getVal1(PL(p)) );
}
void code191(BURS_TreeNode p) {
    EMIT_Commutative(IA32_OR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code192(BURS_TreeNode p) {
    EMIT_Commutative(IA32_OR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO() );
}
void code193(BURS_TreeNode p) {
    EMIT_Commutative(IA32_OR, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO() );
}
void code194(BURS_TreeNode p) {
    EMIT_Commutative(IA32_OR, P(p), MO_S(P(p), DW), MO_S(P(p), DW), Binary.getVal2(PL(p)) );
}
void code195(BURS_TreeNode p) {
    EMIT_Commutative(IA32_OR, P(p), MO_S(P(p), DW), MO_S(P(p), DW), Binary.getVal1(PL(p)) );
}
void code196(BURS_TreeNode p) {
    EMIT_Commutative(IA32_OR, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW), Binary.getVal2(PL(p)) );
}
void code197(BURS_TreeNode p) {
    EMIT_Commutative(IA32_OR, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW), Binary.getVal1(PL(p)) );
}
void code198(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code199(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO() );
}
void code200(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO() );
}
void code201(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), MO_S(P(p), DW), MO_S(P(p), DW), Binary.getVal2(PL(p)) );
}
void code202(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), MO_S(P(p), DW), MO_S(P(p), DW), Binary.getVal1(PL(p)) );
}
void code203(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW), Binary.getVal2(PL(p)) );
}
void code204(BURS_TreeNode p) {
    EMIT_Commutative(IA32_XOR, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW), Binary.getVal1(PL(p)) );
}
void code205(BURS_TreeNode p) {
    EMIT_Unary(IA32_NOT, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code206(BURS_TreeNode p) {
    EMIT_Unary(IA32_NOT, P(p), MO_S(P(p), DW), MO_S(P(p), DW));
}
void code207(BURS_TreeNode p) {
    EMIT_Unary(IA32_NOT, P(p), MO_AS(P(p), DW_S, DW), MO_AS(P(p), DW_S, DW));
}
void code208(BURS_TreeNode p) {
    LONG_ADD(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code209(BURS_TreeNode p) {
    LONG_SUB(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code210(BURS_TreeNode p) {
    LONG_MUL(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code211(BURS_TreeNode p) {
    LONG_NEG(P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code212(BURS_TreeNode p) {
    LONG_SHL(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)), false);
}
void code213(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VRR(p) & 0x7FFFFFFF) <= 63); 
LONG_SHL(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal1(PR(p)), true);
}
void code214(BURS_TreeNode p) {
    LONG_SHR(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)), false);
}
void code215(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VRR(p) & 0x7FFFFFFF) <= 63); 
LONG_SHR(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal1(PR(p)), true);
}
void code216(BURS_TreeNode p) {
    LONG_USHR(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)), false);
}
void code217(BURS_TreeNode p) {
    if (VM.VerifyAssertions) VM._assert((VRR(p) & 0x7FFFFFFF) <= 63); 
LONG_USHR(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal1(PR(p)), true);
}
void code218(BURS_TreeNode p) {
    LONG_AND(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code219(BURS_TreeNode p) {
    LONG_OR(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code220(BURS_TreeNode p) {
    LONG_XOR(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code221(BURS_TreeNode p) {
    LONG_NOT(P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code222(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__B, Unary.getResult(P(p)), Unary.getVal(P(p))));
}
void code223(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__B, Unary.getResult(P(p)), consumeMO()));
}
void code224(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), B), Unary.getVal(PL(p))));
}
void code225(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), B_S, B), Unary.getVal(PL(p))));
}
void code226(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, Unary.getResult(P(p)), Unary.getVal(P(p))))); 
EMIT(MIR_BinaryAcc.mutate(P(p), IA32_AND, Unary.getResult(P(p)).copyRO(), IC(0xFFFF)));
}
void code227(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__W, Unary.getResult(P(p)), setSize(consumeMO(),2)));
}
void code228(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), W), Unary.getVal(PL(p))));
}
void code229(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), W_S, W), Unary.getVal(PL(p))));
}
void code230(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__W, Unary.getResult(P(p)), Unary.getVal(P(p))));
}
void code231(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__W, Unary.getResult(P(p)), consumeMO()));
}
void code232(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), W), Unary.getVal(PL(p))));
}
void code233(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), W_S, W), Unary.getVal(PL(p))));
}
void code234(BURS_TreeNode p) {
    INT_2LONG(P(p), Unary.getResult(P(p)), Unary.getVal(P(p)), true);
}
void code235(BURS_TreeNode p) {
    INT_2LONG(P(p), Unary.getResult(P(p)), consumeMO(), true);
}
void code236(BURS_TreeNode p) {
    INT_2LONG(P(p), Binary.getResult(P(p)), Unary.getVal(PL(p)), false);
}
void code237(BURS_TreeNode p) {
    INT_2LONG(P(p), Binary.getResult(P(p)), consumeMO(), false);
}
void code238(BURS_TreeNode p) {
    Register hr = Binary.getResult(P(p)).getRegister(); 
Register lr = regpool.getSecondReg(hr); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, new RegisterOperand(hr, TypeReference.Int), Unary.getVal(PL(p)).copy())); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(lr, TypeReference.Int), IC(0))));
}
void code239(BURS_TreeNode p) {
    Register hr = Binary.getResult(P(p)).getRegister(); 
Register lr = regpool.getSecondReg(hr); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, new RegisterOperand(hr, TypeReference.Int), setSize(consumeMO(),4))); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(lr, TypeReference.Int), IC(0))));
}
void code240(BURS_TreeNode p) {
    INT_2LONG(P(p), Unary.getResult(P(p)), Unary.getVal(P(p)), false);
}
void code241(BURS_TreeNode p) {
    INT_2LONG(P(p), Unary.getResult(P(p)), consumeMO(), false);
}
void code242(BURS_TreeNode p) {
    Register lh = regpool.getSecondReg(R(Unary.getVal(P(p))).getRegister()); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Unary.getResult(P(p)), new RegisterOperand(lh, TypeReference.Int)));
}
void code243(BURS_TreeNode p) {
    Register lh = regpool.getSecondReg(R(Unary.getVal(PL(p))).getRegister()); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), DW), new RegisterOperand(lh, TypeReference.Int)));
}
void code244(BURS_TreeNode p) {
    Register lh = regpool.getSecondReg(R(Unary.getVal(PL(p))).getRegister()); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), DW_S, DW), new RegisterOperand(lh, TypeReference.Int)));
}
void code245(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, Unary.getResult(P(p)), setSize(consumeMO(),4)));
}
void code246(BURS_TreeNode p) {
    Register uh = Binary.getVal1(PL(p)).asRegister().getRegister(); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Unary.getResult(P(p)), new RegisterOperand(uh, TypeReference.Int)));
}
void code247(BURS_TreeNode p) {
    Register uh = Binary.getVal1(PL(p)).asRegister().getRegister(); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Unary.getResult(P(p)), new RegisterOperand(uh, TypeReference.Int)));
}
void code248(BURS_TreeNode p) {
    MemoryOperand mo = consumeMO(); 
mo.disp = mo.disp.plus(4); 
mo = setSize(mo,4); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Unary.getResult(P(p)), mo));
}
void code249(BURS_TreeNode p) {
    MemoryOperand mo = consumeMO(); 
mo.disp = mo.disp.plus(4); 
mo = setSize(mo,4); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Unary.getResult(P(p)), mo));
}
void code250(BURS_TreeNode p) {
    MemoryOperand mo = consumeMO(); 
mo.disp = mo.disp.plus(4); 
mo = setSize(mo,4); 
pushMO(mo);
}
void code251(BURS_TreeNode p) {
    MemoryOperand mo = consumeMO(); 
mo.disp = mo.disp.plus(4); 
mo = setSize(mo,4); 
pushMO(mo);
}
void code252(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, Move.getResult(P(p)), Move.getVal(P(p))));
}
void code253(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, Move.getResult(P(p)), Move.getVal(P(p))));
}
void code255(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, Move.getResult(P(p)), Move.getVal(P(p))));
}
void code268(BURS_TreeNode p) {
    Register res1 = Move.getResult(P(p)).getRegister();           
Register res2 = regpool.getSecondReg(res1);              
Register val1 = Move.getVal(P(p)).asRegister().getRegister(); 
Register val2 = regpool.getSecondReg(val1);              
EMIT(MIR_Move.mutate(P(p), IA32_MOV, new RegisterOperand(res1, TypeReference.Int), 
                     new RegisterOperand(val1, TypeReference.Int)));               
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(res2, TypeReference.Int), 
                     new RegisterOperand(val2, TypeReference.Int))));
}
void code269(BURS_TreeNode p) {
    Register res1 = Move.getResult(P(p)).getRegister();   
Register res2 = regpool.getSecondReg(res1);      
LongConstantOperand val = LC(Move.getVal(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, new RegisterOperand(res1, TypeReference.Int), IC(val.upper32()))); 
EMIT(CPOS(P(p),MIR_Move.create(IA32_MOV, new RegisterOperand(res2, TypeReference.Int), IC(val.lower32()))));
}
void code270(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, Nullary.getResult(P(p)), new RegisterOperand(getESI(), TypeReference.Int)));
}
void code271(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__B, Load.getResult(P(p)), MO_L(P(p), B)));
}
void code272(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__B, ALoad.getResult(P(p)), MO_AL(P(p), B_S, B)));
}
void code273(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__B, Load.getResult(P(p)), MO_L(P(p), B)));
}
void code274(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__B, ALoad.getResult(P(p)), MO_AL(P(p), B_S, B)));
}
void code275(BURS_TreeNode p) {
    pushMO(setSize(consumeMO(),1));
}
void code276(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__B, Binary.getResult(P(p)), setSize(consumeMO(),1)));
}
void code277(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__B, Unary.getResult(P(p)), setSize(consumeMO(),1)));
}
void code278(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__B, Binary.getResult(P(p)), setSize(consumeMO(),1)));
}
void code279(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__W, Load.getResult(P(p)), MO_L(P(p), W)));
}
void code280(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__W, ALoad.getResult(P(p)), MO_AL(P(p), W_S, W)));
}
void code281(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__W, Load.getResult(P(p)), MO_L(P(p), W)));
}
void code282(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__W, ALoad.getResult(P(p)), MO_AL(P(p), W_S, W)));
}
void code283(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__W, Binary.getResult(P(p)), setSize(consumeMO(),2)));
}
void code284(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__W, Unary.getResult(P(p)), setSize(consumeMO(),2)));
}
void code285(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVSX__W, Unary.getResult(P(p)), setSize(consumeMO(),2)));
}
void code286(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_MOVZX__W, Binary.getResult(P(p)), setSize(consumeMO(),2)));
}
void code287(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, Load.getResult(P(p)), MO_L(P(p), DW)));
}
void code288(BURS_TreeNode p) {
    augmentAddress(Load.getAddress(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Load.getResult(P(p)), 
		     consumeAddress(DW, Load.getLocation(P(p)), Load.getGuard(P(p)))));
}
void code289(BURS_TreeNode p) {
    augmentAddress(Load.getOffset(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Load.getResult(P(p)), 
	             consumeAddress(DW, Load.getLocation(P(p)), Load.getGuard(P(p)))));
}
void code290(BURS_TreeNode p) {
    combineAddresses(); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Load.getResult(P(p)), 
	             consumeAddress(DW, Load.getLocation(P(p)), Load.getGuard(P(p)))));
}
void code291(BURS_TreeNode p) {
    combineAddresses(); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Load.getResult(P(p)), 
	             consumeAddress(DW, Load.getLocation(P(p)), Load.getGuard(P(p)))));
}
void code292(BURS_TreeNode p) {
    augmentAddress(Load.getOffset(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Load.getResult(P(p)), 
	             consumeAddress(DW, Load.getLocation(P(p)), Load.getGuard(P(p)))));
}
void code293(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, ALoad.getResult(P(p)), MO_AL(P(p), DW_S, DW)));
}
void code294(BURS_TreeNode p) {
    RegisterOperand hres = Load.getResult(P(p)); 
RegisterOperand lres = new RegisterOperand(regpool.getSecondReg(hres.getRegister()), TypeReference.Int); 
hres.setType(TypeReference.Int); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, hres, MO_L(P(p), DW, DW).copy()))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, lres, MO_L(P(p), DW)));
}
void code295(BURS_TreeNode p) {
    RegisterOperand hres = ALoad.getResult(P(p)); 
RegisterOperand lres = new RegisterOperand(regpool.getSecondReg(hres.getRegister()), TypeReference.Int); 
hres.setType(TypeReference.Int); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, hres, MO_AL(P(p), QW_S, DW, DW).copy()))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, lres, MO_AL(P(p), QW_S, DW)));
}
void code296(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, Prepare.getResult(P(p)), 
	             MO(Prepare.getAddress(P(p)), Prepare.getOffset(P(p)), DW, 
	                Prepare.getLocation(P(p)), Prepare.getGuard(P(p)))));
}
void code297(BURS_TreeNode p) {
    augmentAddress(Prepare.getAddress(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Prepare.getResult(P(p)), 
		     consumeAddress(DW, Prepare.getLocation(P(p)), Prepare.getGuard(P(p)))));
}
void code298(BURS_TreeNode p) {
    augmentAddress(Prepare.getOffset(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Prepare.getResult(P(p)), 
	             consumeAddress(DW, Prepare.getLocation(P(p)), Prepare.getGuard(P(p)))));
}
void code299(BURS_TreeNode p) {
    combineAddresses(); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Prepare.getResult(P(p)), 
	             consumeAddress(DW, Prepare.getLocation(P(p)), Prepare.getGuard(P(p)))));
}
void code300(BURS_TreeNode p) {
    combineAddresses(); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Prepare.getResult(P(p)), 
	             consumeAddress(DW, Prepare.getLocation(P(p)), Prepare.getGuard(P(p)))));
}
void code301(BURS_TreeNode p) {
    augmentAddress(Prepare.getOffset(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Prepare.getResult(P(p)), 
	             consumeAddress(DW, Prepare.getLocation(P(p)), Prepare.getGuard(P(p)))));
}
void code302(BURS_TreeNode p) {
    augmentAddress(Prepare.getAddress(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, Prepare.getResult(P(p)), 
	             consumeAddress(DW, Prepare.getLocation(P(p)), Prepare.getGuard(P(p)))));
}
void code303(BURS_TreeNode p) {
    RegisterOperand hres = Prepare.getResult(P(p)); 
hres.setType(TypeReference.Int); 
MemoryOperand hmo = MO(Prepare.getAddress(P(p)), Prepare.getOffset(P(p)), DW, 
                       Prepare.getLocation(P(p)), Prepare.getGuard(P(p)), DW); 
RegisterOperand lres = new RegisterOperand(regpool.getSecondReg(hres.getRegister()), TypeReference.Int); 
MemoryOperand lmo = MO(Prepare.getAddress(P(p)) != null ? Prepare.getAddress(P(p)).copy() : null, 
                       Prepare.getOffset(P(p)) != null ? Prepare.getOffset(P(p)).copy() : null, 
                       DW, 
                       Prepare.getLocation(P(p)) != null ? (LocationOperand)Prepare.getLocation(P(p)).copy() : null, 
                       Prepare.getGuard(P(p)) != null ? Prepare.getGuard(P(p)).copy() : null); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, hres, hmo))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, lres, lmo));
}
void code304(BURS_TreeNode p) {
    ATTEMPT(Attempt.getResult(P(p)), 
              MO(Attempt.getAddress(P(p)), Attempt.getOffset(P(p)), DW, Attempt.getLocation(P(p)), Attempt.getGuard(P(p))), 
              Attempt.getOldValue(P(p)), Attempt.getNewValue(P(p)));
}
void code305(BURS_TreeNode p) {
    augmentAddress(Attempt.getAddress(P(p))); 
ATTEMPT(Attempt.getResult(P(p)), 
              consumeAddress(DW, Attempt.getLocation(P(p)), Attempt.getGuard(P(p))), 
              Attempt.getOldValue(P(p)), Attempt.getNewValue(P(p)));
}
void code306(BURS_TreeNode p) {
    augmentAddress(Attempt.getOffset(P(p))); 
ATTEMPT(Attempt.getResult(P(p)), 
              consumeAddress(DW, Attempt.getLocation(P(p)), Attempt.getGuard(P(p))), 
              Attempt.getOldValue(P(p)), Attempt.getNewValue(P(p)));
}
void code307(BURS_TreeNode p) {
    combineAddresses(); 
ATTEMPT(Attempt.getResult(P(p)), 
              consumeAddress(DW, Attempt.getLocation(P(p)), Attempt.getGuard(P(p))), 
              Attempt.getOldValue(P(p)), Attempt.getNewValue(P(p)));
}
void code308(BURS_TreeNode p) {
    combineAddresses(); 
ATTEMPT(Attempt.getResult(P(p)), 
              consumeAddress(DW, Attempt.getLocation(P(p)), Attempt.getGuard(P(p))), 
              Attempt.getOldValue(P(p)), Attempt.getNewValue(P(p)));
}
void code309(BURS_TreeNode p) {
    augmentAddress(Attempt.getOffset(P(p))); 
ATTEMPT(Attempt.getResult(P(p)), 
              consumeAddress(DW, Attempt.getLocation(P(p)), Attempt.getGuard(P(p))), 
              Attempt.getOldValue(P(p)), Attempt.getNewValue(P(p)));
}
void code310(BURS_TreeNode p) {
    augmentAddress(Attempt.getAddress(P(p))); 
ATTEMPT(Attempt.getResult(P(p)), 
              consumeAddress(DW, Attempt.getLocation(P(p)), Attempt.getGuard(P(p))), 
              Attempt.getOldValue(P(p)), Attempt.getNewValue(P(p)));
}
void code311(BURS_TreeNode p) {
    ATTEMPT_IFCMP(MO(Attempt.getAddress(PL(p)), Attempt.getOffset(PL(p)), DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)).flipCode(), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code312(BURS_TreeNode p) {
    augmentAddress(Attempt.getAddress(PL(p))); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)).flipCode(), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code313(BURS_TreeNode p) {
    augmentAddress(Attempt.getOffset(PL(p))); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)).flipCode(), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code314(BURS_TreeNode p) {
    combineAddresses(); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)).flipCode(), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code315(BURS_TreeNode p) {
    combineAddresses(); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)).flipCode(), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code316(BURS_TreeNode p) {
    augmentAddress(Attempt.getOffset(PL(p))); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)).flipCode(), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code317(BURS_TreeNode p) {
    augmentAddress(Attempt.getAddress(PL(p))); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)).flipCode(), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code318(BURS_TreeNode p) {
    ATTEMPT_IFCMP(MO(Attempt.getAddress(PL(p)), Attempt.getOffset(PL(p)), DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code319(BURS_TreeNode p) {
    augmentAddress(Attempt.getAddress(PL(p))); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code320(BURS_TreeNode p) {
    augmentAddress(Attempt.getOffset(PL(p))); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code321(BURS_TreeNode p) {
    combineAddresses(); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code322(BURS_TreeNode p) {
    combineAddresses(); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code323(BURS_TreeNode p) {
    augmentAddress(Attempt.getOffset(PL(p))); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code324(BURS_TreeNode p) {
    augmentAddress(Attempt.getAddress(PL(p))); 
ATTEMPT_IFCMP(consumeAddress(DW, Attempt.getLocation(PL(p)), Attempt.getGuard(PL(p))), 
	            Attempt.getOldValue(PL(p)), Attempt.getNewValue(PL(p)), 
		    IfCmp.getCond(P(p)), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p)));
}
void code325(BURS_TreeNode p) {
    ATTEMPT_LONG(Attempt.getResult(P(p)), 
              MO(Attempt.getAddress(P(p)), Attempt.getOffset(P(p)), DW, Attempt.getLocation(P(p)), Attempt.getGuard(P(p))), 
              Attempt.getOldValue(P(p)), Attempt.getNewValue(P(p)));
}
void code326(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), B), Store.getValue(P(p))));
}
void code327(BURS_TreeNode p) {
    Register tmp = regpool.getInteger(); 
EMIT(CPOS(PL(p), MIR_Move.create(IA32_MOV, new RegisterOperand(tmp, TypeReference.Int), consumeMO()))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), B), new RegisterOperand(tmp, TypeReference.Int)));
}
void code328(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), B_S, B), AStore.getValue(P(p))));
}
void code329(BURS_TreeNode p) {
    Register tmp = regpool.getInteger(); 
EMIT(CPOS(PL(p), MIR_Move.create(IA32_MOV, new RegisterOperand(tmp, TypeReference.Int), consumeMO()))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), B_S, B), new RegisterOperand(tmp, TypeReference.Int)));
}
void code330(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), W), Store.getValue(P(p))));
}
void code331(BURS_TreeNode p) {
    Register tmp = regpool.getInteger(); 
EMIT(CPOS(PL(p), MIR_Move.create(IA32_MOV, new RegisterOperand(tmp, TypeReference.Int), consumeMO()))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), W), new RegisterOperand(tmp, TypeReference.Int)));
}
void code332(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), W_S, W), AStore.getValue(P(p))));
}
void code333(BURS_TreeNode p) {
    Register tmp = regpool.getInteger(); 
EMIT(CPOS(PL(p), MIR_Move.create(IA32_MOV, new RegisterOperand(tmp, TypeReference.Int), consumeMO()))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), W_S, W), new RegisterOperand(tmp, TypeReference.Int)));
}
void code334(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), DW), Store.getValue(P(p))));
}
void code335(BURS_TreeNode p) {
    augmentAddress(Store.getAddress(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, 
                     consumeAddress(DW, Store.getLocation(P(p)), Store.getGuard(P(p))), 
		     Store.getValue(P(p))));
}
void code336(BURS_TreeNode p) {
    augmentAddress(Store.getOffset(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, 
                     consumeAddress(DW, Store.getLocation(P(p)), Store.getGuard(P(p))), 
		     Store.getValue(P(p))));
}
void code337(BURS_TreeNode p) {
    combineAddresses(); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV,  
                    consumeAddress(DW, Store.getLocation(P(p)), Store.getGuard(P(p))), 
                    Store.getValue(P(p))));
}
void code338(BURS_TreeNode p) {
    combineAddresses(); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV,  
                    consumeAddress(DW, Store.getLocation(P(p)), Store.getGuard(P(p))), 
                    Store.getValue(P(p))));
}
void code339(BURS_TreeNode p) {
    augmentAddress(Store.getOffset(P(p))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV,  
	             consumeAddress(DW, Store.getLocation(P(p)), Store.getGuard(P(p))), 
		     Store.getValue(P(p))));
}
void code340(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), DW_S, DW), AStore.getValue(P(p))));
}
void code341(BURS_TreeNode p) {
    RegisterOperand hval = (RegisterOperand)Store.getValue(P(p)); 
hval.setType(TypeReference.Int); 
RegisterOperand lval = new RegisterOperand(regpool.getSecondReg(hval.getRegister()), TypeReference.Int); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, MO_S(P(p), DW, DW).copy(), hval))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), DW), lval));
}
void code342(BURS_TreeNode p) {
    LongConstantOperand val = LC(Store.getValue(P(p))); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, MO_S(P(p), DW, DW).copy(), IC(val.upper32())))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_S(P(p), DW), IC(val.lower32())));
}
void code343(BURS_TreeNode p) {
    RegisterOperand hval = (RegisterOperand)AStore.getValue(P(p)); 
hval.setType(TypeReference.Int); 
RegisterOperand lval = new RegisterOperand(regpool.getSecondReg(hval.getRegister()), TypeReference.Int); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, MO_AS(P(p), QW_S, DW, DW).copy(), hval))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), QW_S, DW), lval));
}
void code344(BURS_TreeNode p) {
    LongConstantOperand val = LC(AStore.getValue(P(p))); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, MO_AS(P(p), QW_S, DW, DW).copy(), IC(val.upper32())))); 
EMIT(MIR_Move.mutate(P(p), IA32_MOV, MO_AS(P(p), QW_S, DW), IC(val.lower32())));
}
void code345(BURS_TreeNode p) {
    IFCMP(P(p), IfCmp.getGuardResult(P(p)), IfCmp.getVal1(P(p)), IfCmp.getVal2(P(p)), IfCmp.getCond(P(p)));
}
void code346(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(CPOS(P(p), MIR_Test.create(IA32_TEST, IfCmp.getVal1(P(p)), IfCmp.getVal1(P(p)).copy()))); 
EMIT(MIR_CondBranch.mutate(P(p), IA32_JCC, COND(IfCmp.getCond(P(p))), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p))));
}
void code347(BURS_TreeNode p) {
    IFCMP(P(p), IfCmp.getGuardResult(P(p)), consumeMO(), IfCmp.getVal2(P(p)), IfCmp.getCond(P(p)));
}
void code348(BURS_TreeNode p) {
    IFCMP(P(p), IfCmp.getGuardResult(P(p)), consumeMO(), IfCmp.getVal2(P(p)), IfCmp.getCond(P(p)));
}
void code349(BURS_TreeNode p) {
    IFCMP(P(p), IfCmp.getGuardResult(P(p)), IfCmp.getVal1(P(p)), consumeMO(), IfCmp.getCond(P(p)));
}
void code350(BURS_TreeNode p) {
    IFCMP(P(p), IfCmp.getGuardResult(P(p)), consumeMO(), IfCmp.getVal2(P(p)), IfCmp.getCond(P(p)));
}
void code351(BURS_TreeNode p) {
    IFCMP(P(p), IfCmp.getGuardResult(P(p)), consumeMO(), IfCmp.getVal2(P(p)), IfCmp.getCond(P(p)));
}
void code352(BURS_TreeNode p) {
    IFCMP(P(p), IfCmp.getGuardResult(P(p)), IfCmp.getVal1(P(p)), consumeMO(), IfCmp.getCond(P(p)));
}
void code353(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(MIR_CondBranch.mutate(P(p), IA32_JCC, COND(consumeCOND()), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p))));
}
void code354(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(MIR_CondBranch.mutate(P(p), IA32_JCC, COND(consumeCOND().flipCode()), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p))));
}
void code355(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(MIR_CondBranch.mutate(P(p), IA32_JCC, COND(IfCmp.getCond(P(p))), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p))));
}
void code356(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(MIR_CondBranch.mutate(P(p), IA32_JCC, COND(IfCmp.getCond(P(p))), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p))));
}
void code357(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(MIR_CondBranch.mutate(P(p), IA32_JCC, COND(BIT_TEST(VR(p), IfCmp.getCond(P(p)))), IfCmp.getTarget(P(p)), IfCmp.getBranchProfile(P(p))));
}
void code358(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp2.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, IfCmp2.getVal1(P(p)), IfCmp2.getVal2(P(p))))); 
EMIT(MIR_CondBranch2.mutate(P(p), IA32_JCC2,                                  
	                    COND(IfCmp2.getCond1(P(p))), IfCmp2.getTarget1(P(p)),IfCmp2.getBranchProfile1(P(p)), 
	                    COND(IfCmp2.getCond2(P(p))), IfCmp2.getTarget2(P(p)), IfCmp2.getBranchProfile2(P(p))));
}
void code359(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp2.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, consumeMO(), IfCmp2.getVal2(P(p))))); 
EMIT(MIR_CondBranch2.mutate(P(p), IA32_JCC2,                                  
	                    COND(IfCmp2.getCond1(P(p))), IfCmp2.getTarget1(P(p)),IfCmp2.getBranchProfile1(P(p)), 
	                    COND(IfCmp2.getCond2(P(p))), IfCmp2.getTarget2(P(p)), IfCmp2.getBranchProfile2(P(p))));
}
void code360(BURS_TreeNode p) {
    EMIT(CPOS(P(p), Move.create(GUARD_MOVE, IfCmp2.getGuardResult(P(p)), new TrueGuardOperand()))); 
EMIT(CPOS(P(p), MIR_Compare.create(IA32_CMP, consumeMO(), IfCmp2.getVal1(P(p))))); 
EMIT(MIR_CondBranch2.mutate(P(p), IA32_JCC2,                                  
	                    COND(IfCmp2.getCond1(P(p)).flipOperands()), IfCmp2.getTarget1(P(p)),IfCmp2.getBranchProfile1(P(p)), 
	                    COND(IfCmp2.getCond2(P(p)).flipOperands()), IfCmp2.getTarget2(P(p)), IfCmp2.getBranchProfile2(P(p))));
}
void code361(BURS_TreeNode p) {
    EMIT(P(p)); //  Leave for ComplexLIR2MIRExpansion
}
void code362(BURS_TreeNode p) {
    LONG_CMP(P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code363(BURS_TreeNode p) {
    EMIT(MIR_Branch.mutate(P(p), IA32_JMP, Goto.getTarget(P(p))));
}
void code364(BURS_TreeNode p) {
    EMIT(MIR_CacheOp.mutate(P(p), IA32_PREFETCHNTA, R(CacheOp.getRef(P(p)))));
}
void code365(BURS_TreeNode p) {
    EMIT(P(p)); // Pass through to maintain barrier semantics for code motion
}
void code366(BURS_TreeNode p) {
    EMIT(P(p)); // Pass through to maintain barrier semantics for code motion
}
void code367(BURS_TreeNode p) {
    EMIT(MIR_Empty.mutate(P(p), IA32_PAUSE));
}
void code368(BURS_TreeNode p) {
    EMIT(MIR_Return.mutate(P(p), IA32_RET, null, null, null));
}
void code369(BURS_TreeNode p) {
    EMIT(MIR_Return.mutate(P(p), IA32_RET, null, Return.getVal(P(p)), null));
}
void code370(BURS_TreeNode p) {
    RegisterOperand ret = R(Return.getVal(P(p)));            
RegisterOperand ret2 = null;	                            
if (ret.getType().isLongType()) {                                 
  ret.setType(TypeReference.Int);                           
  ret2 = new RegisterOperand(regpool.getSecondReg(ret.getRegister()), TypeReference.Int); 
}                                                            
EMIT(MIR_Return.mutate(P(p), IA32_RET, null, ret, ret2));
}
void code371(BURS_TreeNode p) {
    LongConstantOperand val = LC(Return.getVal(P(p))); 
EMIT(MIR_Return.mutate(P(p), IA32_RET, null, IC(val.upper32()), IC(val.lower32())));
}
void code372(BURS_TreeNode p) {
    CALL(P(p), Call.getAddress(P(p)));
}
void code373(BURS_TreeNode p) {
    CALL(P(p), Call.getAddress(P(p)));
}
void code374(BURS_TreeNode p) {
    CALL(P(p), MO_L(PL(p), DW));
}
void code375(BURS_TreeNode p) {
    RegisterOperand temp = regpool.makeTemp(TypeReference.Int); 
EMIT(MIR_Move.create(IA32_MOV, temp, Call.getAddress(P(p)))); 
CALL(P(p), temp.copyRO());
}
void code376(BURS_TreeNode p) {
    SYSCALL(P(p), Call.getAddress(P(p)));
}
void code377(BURS_TreeNode p) {
    SYSCALL(P(p), MO_L(PL(p), DW));
}
void code378(BURS_TreeNode p) {
    RegisterOperand temp = regpool.makeTemp(TypeReference.Int); 
EMIT(MIR_Move.create(IA32_MOV, temp, Call.getAddress(P(p)))); 
SYSCALL(P(p), temp.copyRO());
}
void code379(BURS_TreeNode p) {
    GET_TIME_BASE(P(p), Nullary.getResult(P(p)));
}
void code380(BURS_TreeNode p) {
    OSR(burs, P(p));
}
void code381(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_BT, Binary.getVal1(PL(p)).copy(), Binary.getVal1(PLR(p)).copy()));
}
void code382(BURS_TreeNode p) {
    Register tmp = regpool.getInteger(); 
if (VM.VerifyAssertions) VM._assert((VLRR(p) & 0x7FFFFFFF) <= 31); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(tmp, TypeReference.Int), Binary.getVal1(PLR(p))))); 
EMIT(CPOS(P(p), MIR_BinaryAcc.create(IA32_AND, new RegisterOperand(tmp, TypeReference.Int), IC(VLRR(p))))); 
EMIT(MIR_Test.mutate(P(p), IA32_BT, consumeMO(), new RegisterOperand(tmp, TypeReference.Int)));
}
void code383(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_BT, Binary.getVal1(PL(p)).copy(), IC(VLR(p))));
}
void code384(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_BT, Binary.getVal1(PL(p)).copy(), Binary.getVal1(PLR(p)).copy()));
}
void code385(BURS_TreeNode p) {
    Register tmp = regpool.getInteger(); 
if (VM.VerifyAssertions) VM._assert((VLRR(p) & 0x7FFFFFFF) <= 31); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(tmp, TypeReference.Int), Binary.getVal1(PLR(p))))); 
EMIT(CPOS(P(p), MIR_BinaryAcc.create(IA32_AND, new RegisterOperand(tmp, TypeReference.Int), IC(VLRR(p))))); 
EMIT(MIR_Test.mutate(P(p), IA32_BT, consumeMO(), new RegisterOperand(tmp, TypeReference.Int)));
}
void code386(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_BT, Binary.getVal1(PL(p)).copy(), IC(VLR(p))));
}
void code387(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_BT, Binary.getVal2(P(p)), Binary.getVal1(PLR(p)).copy()));
}
void code388(BURS_TreeNode p) {
    Register tmp = regpool.getInteger(); 
if (VM.VerifyAssertions) VM._assert((VLRR(p) & 0x7FFFFFFF) <= 31); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(tmp, TypeReference.Int), Binary.getVal1(PLR(p))))); 
EMIT(CPOS(P(p), MIR_BinaryAcc.create(IA32_AND, new RegisterOperand(tmp, TypeReference.Int), IC(VLRR(p))))); 
EMIT(MIR_Test.mutate(P(p), IA32_BT, consumeMO(), new RegisterOperand(tmp, TypeReference.Int)));
}
void code389(BURS_TreeNode p) {
    EMIT(MIR_Test.mutate(P(p), IA32_BT, Binary.getVal1(P(p)), Binary.getVal1(PRR(p)).copy()));
}
void code390(BURS_TreeNode p) {
    Register tmp = regpool.getInteger(); 
if (VM.VerifyAssertions) VM._assert((VRRR(p) & 0x7FFFFFFF) <= 31); 
EMIT(CPOS(P(p), MIR_Move.create(IA32_MOV, new RegisterOperand(tmp, TypeReference.Int), Binary.getVal1(PRR(p))))); 
EMIT(CPOS(P(p), MIR_BinaryAcc.create(IA32_AND, new RegisterOperand(tmp, TypeReference.Int), IC(VRRR(p))))); 
EMIT(MIR_Test.mutate(P(p), IA32_BT, consumeMO(), new RegisterOperand(tmp, TypeReference.Int)));
}
void code391(BURS_TreeNode p) {
    SSE2_COP(IA32_ADDSS, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code392(BURS_TreeNode p) {
    SSE2_COP(IA32_ADDSS, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code393(BURS_TreeNode p) {
    SSE2_COP(IA32_ADDSS, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO());
}
void code394(BURS_TreeNode p) {
    SSE2_COP(IA32_ADDSD, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), Binary.getVal1(P(p)));
}
void code395(BURS_TreeNode p) {
    SSE2_COP(IA32_ADDSD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code396(BURS_TreeNode p) {
    SSE2_COP(IA32_ADDSD, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO());
}
void code397(BURS_TreeNode p) {
    SSE2_NCOP(IA32_SUBSS, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code398(BURS_TreeNode p) {
    SSE2_NCOP(IA32_SUBSS, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code399(BURS_TreeNode p) {
    SSE2_NCOP(IA32_SUBSD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code400(BURS_TreeNode p) {
    SSE2_NCOP(IA32_SUBSD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code401(BURS_TreeNode p) {
    SSE2_COP(IA32_MULSS, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code402(BURS_TreeNode p) {
    SSE2_COP(IA32_MULSS, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code403(BURS_TreeNode p) {
    SSE2_COP(IA32_MULSS, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO());
}
void code404(BURS_TreeNode p) {
    SSE2_COP(IA32_MULSD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code405(BURS_TreeNode p) {
    SSE2_COP(IA32_MULSD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code406(BURS_TreeNode p) {
    SSE2_COP(IA32_MULSD, P(p), Binary.getResult(P(p)), Binary.getVal2(P(p)), consumeMO());
}
void code407(BURS_TreeNode p) {
    SSE2_NCOP(IA32_DIVSS, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code408(BURS_TreeNode p) {
    SSE2_NCOP(IA32_DIVSS, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code409(BURS_TreeNode p) {
    SSE2_NCOP(IA32_DIVSD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), Binary.getVal2(P(p)));
}
void code410(BURS_TreeNode p) {
    SSE2_NCOP(IA32_DIVSD, P(p), Binary.getResult(P(p)), Binary.getVal1(P(p)), consumeMO());
}
void code411(BURS_TreeNode p) {
    SSE2_NEG(true, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code412(BURS_TreeNode p) {
    SSE2_NEG(false, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code413(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_SQRTSS, Unary.getResult(P(p)), Unary.getVal(P(p))));
}
void code414(BURS_TreeNode p) {
    EMIT(MIR_Unary.mutate(P(p), IA32_SQRTSD, Unary.getResult(P(p)), Unary.getVal(P(p))));
}
void code415(BURS_TreeNode p) {
    SSE2_X87_REM(P(p));
}
void code416(BURS_TreeNode p) {
    SSE2_X87_REM(P(p));
}
void code417(BURS_TreeNode p) {
    SSE2_X87_FROMLONG(P(p));
}
void code418(BURS_TreeNode p) {
    SSE2_X87_FROMLONG(P(p));
}
void code419(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVSS, Move.getResult(P(p)), Move.getVal(P(p))));
}
void code420(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVSD, Move.getResult(P(p)), Move.getVal(P(p))));
}
void code421(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVLPD, Load.getResult(P(p)), MO_L(P(p), QW)));
}
void code422(BURS_TreeNode p) {
    pushMO(MO_L(P(p), QW));
}
void code423(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVLPD, ALoad.getResult(P(p)), MO_AL(P(p), QW_S, QW)));
}
void code424(BURS_TreeNode p) {
    pushMO(MO_AL(P(p), QW_S, QW));
}
void code425(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVSS, Load.getResult(P(p)), MO_L(P(p), DW)));
}
void code426(BURS_TreeNode p) {
    pushMO(MO_L(P(p), DW));
}
void code427(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVSS, ALoad.getResult(P(p)), MO_AL(P(p), DW_S, DW)));
}
void code428(BURS_TreeNode p) {
    pushMO(MO_AL(P(p), DW_S, DW));
}
void code429(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVLPD, MO_S(P(p), QW), Store.getValue(P(p))));
}
void code430(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVLPD, MO_AS(P(p), QW_S, QW), AStore.getValue(P(p))));
}
void code431(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVSS, MO_S(P(p), DW), Store.getValue(P(p))));
}
void code432(BURS_TreeNode p) {
    EMIT(MIR_Move.mutate(P(p), IA32_MOVSS, MO_AS(P(p), DW_S, DW), AStore.getValue(P(p))));
}
void code433(BURS_TreeNode p) {
    SSE2_CONV(IA32_CVTSI2SS, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code434(BURS_TreeNode p) {
    SSE2_CONV(IA32_CVTSI2SS, P(p), Unary.getResult(P(p)), consumeMO());
}
void code435(BURS_TreeNode p) {
    SSE2_CONV(IA32_CVTSI2SD, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code436(BURS_TreeNode p) {
    SSE2_CONV(IA32_CVTSI2SD, P(p), Unary.getResult(P(p)), consumeMO());
}
void code437(BURS_TreeNode p) {
    SSE2_CONV(IA32_CVTSS2SD, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code438(BURS_TreeNode p) {
    SSE2_CONV(IA32_CVTSS2SD, P(p), Unary.getResult(P(p)), consumeMO());
}
void code439(BURS_TreeNode p) {
    SSE2_CONV(IA32_CVTSD2SS, P(p), Unary.getResult(P(p)), Unary.getVal(P(p)));
}
void code440(BURS_TreeNode p) {
    SSE2_CONV(IA32_CVTSD2SS, P(p), Unary.getResult(P(p)), consumeMO());
}
void code441(BURS_TreeNode p) {
    EMIT(P(p)); /* leave for complex operators */
}
void code442(BURS_TreeNode p) {
    EMIT(P(p)); /* leave for complex operators */
}
void code443(BURS_TreeNode p) {
    EMIT(P(p)); /* leave for complex operators */
}
void code444(BURS_TreeNode p) {
    EMIT(P(p)); /* leave for complex operators */
}
void code445(BURS_TreeNode p) {
    SSE2_FPR2GPR_32(P(p));
}
void code447(BURS_TreeNode p) {
    SSE2_FPR2GPR_64(P(p));
}
void code449(BURS_TreeNode p) {
    SSE2_GPR2FPR_32(P(p));
}
void code451(BURS_TreeNode p) {
    SSE2_GPR2FPR_64(P(p));
}
void code453(BURS_TreeNode p) {
    SSE2_FPCONSTANT(P(p));
}
void code454(BURS_TreeNode p) {
    pushMO(MO_MC(P(p)));
}
void code455(BURS_TreeNode p) {
    pushMO(MO_MC(P(p)));
}
void code456(BURS_TreeNode p) {
    EMIT(MIR_Empty.mutate(P(p), IA32_FNINIT));
}
void code457(BURS_TreeNode p) {
    SSE2_IFCMP(IA32_UCOMISS, P(p), IfCmp.getClearVal1(P(p)), IfCmp.getClearVal2(P(p)));
}
void code458(BURS_TreeNode p) {
    SSE2_IFCMP(IA32_UCOMISS, P(p), IfCmp.getClearVal1(P(p)), consumeMO());
}
void code459(BURS_TreeNode p) {
    IfCmp.getCond(P(p)).flipOperands(); SSE2_IFCMP(IA32_UCOMISS, P(p), IfCmp.getClearVal2(P(p)), consumeMO());
}
void code460(BURS_TreeNode p) {
    SSE2_IFCMP(IA32_UCOMISD, P(p), IfCmp.getClearVal1(P(p)), IfCmp.getClearVal2(P(p)));
}
void code461(BURS_TreeNode p) {
    SSE2_IFCMP(IA32_UCOMISD, P(p), IfCmp.getClearVal1(P(p)), consumeMO());
}
void code462(BURS_TreeNode p) {
    IfCmp.getCond(P(p)).flipOperands(); SSE2_IFCMP(IA32_UCOMISD, P(p), IfCmp.getClearVal2(P(p)), consumeMO());
}
void code463(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(CondMove.getVal1(P(p)).isFloat() ? IA32_UCOMISS : IA32_UCOMISD,      CondMove.getVal1(P(p)), CondMove.getVal2(P(p))))); CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)).translateUNSIGNED(),          CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code464(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_UCOMISS, CondMove.getVal1(P(p)), consumeMO()))); CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)).translateUNSIGNED(),          CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code465(BURS_TreeNode p) {
    EMIT(CPOS(P(p), MIR_Compare.create(IA32_UCOMISD, CondMove.getVal1(P(p)), consumeMO()))); CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)).translateUNSIGNED(),          CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code466(BURS_TreeNode p) {
    CondMove.getCond(P(p)).flipOperands(); EMIT(CPOS(P(p), MIR_Compare.create(IA32_UCOMISS, CondMove.getVal1(P(p)), consumeMO()))); CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)).translateUNSIGNED(),          CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code467(BURS_TreeNode p) {
    CondMove.getCond(P(p)).flipOperands(); EMIT(CPOS(P(p), MIR_Compare.create(IA32_UCOMISD, CondMove.getVal1(P(p)), consumeMO()))); CMOV_MOV(P(p), CondMove.getResult(P(p)), CondMove.getCond(P(p)).translateUNSIGNED(),          CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code468(BURS_TreeNode p) {
    SSE2_FCMP_FCMOV(P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), CondMove.getVal2(P(p)),                 CondMove.getCond(P(p)), CondMove.getTrueValue(P(p)), CondMove.getFalseValue(P(p)));
}
void code469(BURS_TreeNode p) {
    SSE2_FCMP_FCMOV(P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), CondMove.getVal2(P(p)),                 CondMove.getCond(P(p)), CondMove.getTrueValue(P(p)), consumeMO());
}
void code470(BURS_TreeNode p) {
    SSE2_FCMP_FCMOV(P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), CondMove.getVal2(P(p)),                 CondMove.getCond(P(p)), CondMove.getTrueValue(P(p)), consumeMO());
}
void code471(BURS_TreeNode p) {
    SSE2_FCMP_FCMOV(P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), CondMove.getVal2(P(p)),                 CondMove.getCond(P(p)), consumeMO(), CondMove.getFalseValue(P(p)));
}
void code472(BURS_TreeNode p) {
    SSE2_FCMP_FCMOV(P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), CondMove.getVal2(P(p)),                 CondMove.getCond(P(p)), consumeMO(), CondMove.getFalseValue(P(p)));
}
void code473(BURS_TreeNode p) {
    SSE2_FCMP_FCMOV(P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), consumeMO(),                 CondMove.getCond(P(p)), CondMove.getFalseValue(P(p)), CondMove.getTrueValue(P(p)));
}
void code474(BURS_TreeNode p) {
    SSE2_FCMP_FCMOV(P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)), consumeMO(),                 CondMove.getCond(P(p)), CondMove.getFalseValue(P(p)), CondMove.getTrueValue(P(p)));
}
void code475(BURS_TreeNode p) {
    SSE2_ABS(true, P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)));
}
void code476(BURS_TreeNode p) {
    SSE2_ABS(true, P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)));
}
void code477(BURS_TreeNode p) {
    SSE2_ABS(true, P(p), CondMove.getResult(P(p)), CondMove.getVal2(P(p)));
}
void code478(BURS_TreeNode p) {
    SSE2_ABS(true, P(p), CondMove.getResult(P(p)), CondMove.getVal2(P(p)));
}
void code479(BURS_TreeNode p) {
    SSE2_ABS(false, P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)));
}
void code480(BURS_TreeNode p) {
    SSE2_ABS(false, P(p), CondMove.getResult(P(p)), CondMove.getVal1(P(p)));
}
void code481(BURS_TreeNode p) {
    SSE2_ABS(false, P(p), CondMove.getResult(P(p)), CondMove.getVal2(P(p)));
}
void code482(BURS_TreeNode p) {
    SSE2_ABS(false, P(p), CondMove.getResult(P(p)), CondMove.getVal2(P(p)));
}
void code483(BURS_TreeNode p) {
    RegisterOperand temp = regpool.makeTemp(TypeReference.Double); EMIT(MIR_Move.mutate(PL(p), IA32_MOVQ, temp, consumeMO())); EMIT(MIR_Move.mutate(P(p), IA32_MOVQ, MO_AS(P(p), QW_S, QW), temp.copyRO()));
}
void code484(BURS_TreeNode p) {
    RegisterOperand temp = regpool.makeTemp(TypeReference.Double); EMIT(MIR_Move.mutate(PL(p), IA32_MOVQ, temp, consumeMO())); EMIT(MIR_Move.mutate(P(p), IA32_MOVQ, MO_S(P(p), QW), temp.copyRO()));
}

public void code(BURS_TreeNode p, int  n, int ruleno) {
  switch(ruleno) {
  case 16: code16(p); break;
  case 17: code17(p); break;
  case 18: code18(p); break;
  case 19: code19(p); break;
  case 22: code22(p); break;
  case 23: code23(p); break;
  case 24: code24(p); break;
  case 25: code25(p); break;
  case 28: code28(p); break;
  case 29: code29(p); break;
  case 34: code34(p); break;
  case 35: code35(p); break;
  case 38: code38(p); break;
  case 39: code39(p); break;
  case 40: code40(p); break;
  case 41: code41(p); break;
  case 42: code42(p); break;
  case 43: code43(p); break;
  case 44: code44(p); break;
  case 45: code45(p); break;
  case 46: code46(p); break;
  case 47: code47(p); break;
  case 48: code48(p); break;
  case 49: code49(p); break;
  case 50: code50(p); break;
  case 51: code51(p); break;
  case 52: code52(p); break;
  case 53: code53(p); break;
  case 54: code54(p); break;
  case 55: code55(p); break;
  case 56: code56(p); break;
  case 57: code57(p); break;
  case 58: code58(p); break;
  case 59: code59(p); break;
  case 60: code60(p); break;
  case 61: code61(p); break;
  case 62: code62(p); break;
  case 64: code64(p); break;
  case 65: code65(p); break;
  case 66: code66(p); break;
  case 67: code67(p); break;
  case 68: code68(p); break;
  case 69: code69(p); break;
  case 70: code70(p); break;
  case 71: code71(p); break;
  case 72: code72(p); break;
  case 73: code73(p); break;
  case 74: code74(p); break;
  case 75: code75(p); break;
  case 76: code76(p); break;
  case 77: code77(p); break;
  case 78: code78(p); break;
  case 79: code79(p); break;
  case 80: code80(p); break;
  case 81: code81(p); break;
  case 82: code82(p); break;
  case 83: code83(p); break;
  case 84: code84(p); break;
  case 85: code85(p); break;
  case 86: code86(p); break;
  case 87: code87(p); break;
  case 88: code88(p); break;
  case 89: code89(p); break;
  case 90: code90(p); break;
  case 91: code91(p); break;
  case 92: code92(p); break;
  case 93: code93(p); break;
  case 95: code95(p); break;
  case 96: code96(p); break;
  case 97: code97(p); break;
  case 98: code98(p); break;
  case 99: code99(p); break;
  case 100: code100(p); break;
  case 101: code101(p); break;
  case 102: code102(p); break;
  case 103: code103(p); break;
  case 104: code104(p); break;
  case 105: code105(p); break;
  case 106: code106(p); break;
  case 107: code107(p); break;
  case 108: code108(p); break;
  case 109: code109(p); break;
  case 110: code110(p); break;
  case 111: code111(p); break;
  case 112: code112(p); break;
  case 113: code113(p); break;
  case 114: code114(p); break;
  case 115: code115(p); break;
  case 116: code116(p); break;
  case 117: code117(p); break;
  case 118: code118(p); break;
  case 119: code119(p); break;
  case 120: code120(p); break;
  case 121: code121(p); break;
  case 122: code122(p); break;
  case 123: code123(p); break;
  case 124: code124(p); break;
  case 125: code125(p); break;
  case 126: code126(p); break;
  case 127: code127(p); break;
  case 128: code128(p); break;
  case 129: code129(p); break;
  case 130: code130(p); break;
  case 131: code131(p); break;
  case 132: code132(p); break;
  case 133: code133(p); break;
  case 134: code134(p); break;
  case 135: code135(p); break;
  case 136: code136(p); break;
  case 137: code137(p); break;
  case 138: code138(p); break;
  case 139: code139(p); break;
  case 140: code140(p); break;
  case 141: code141(p); break;
  case 142: code142(p); break;
  case 143: code143(p); break;
  case 144: code144(p); break;
  case 145: code145(p); break;
  case 146: code146(p); break;
  case 147: code147(p); break;
  case 148: code148(p); break;
  case 149: code149(p); break;
  case 150: code150(p); break;
  case 151: code151(p); break;
  case 152: code152(p); break;
  case 153: code153(p); break;
  case 154: code154(p); break;
  case 155: code155(p); break;
  case 156: code156(p); break;
  case 157: code157(p); break;
  case 158: code158(p); break;
  case 159: code159(p); break;
  case 160: code160(p); break;
  case 161: code161(p); break;
  case 162: code162(p); break;
  case 163: code163(p); break;
  case 164: code164(p); break;
  case 165: code165(p); break;
  case 166: code166(p); break;
  case 167: code167(p); break;
  case 168: code168(p); break;
  case 169: code169(p); break;
  case 170: code170(p); break;
  case 171: code171(p); break;
  case 172: code172(p); break;
  case 173: code173(p); break;
  case 174: code174(p); break;
  case 175: code175(p); break;
  case 176: code176(p); break;
  case 177: code177(p); break;
  case 178: code178(p); break;
  case 179: code179(p); break;
  case 180: code180(p); break;
  case 181: code181(p); break;
  case 182: code182(p); break;
  case 183: code183(p); break;
  case 184: code184(p); break;
  case 185: code185(p); break;
  case 186: code186(p); break;
  case 187: code187(p); break;
  case 188: code188(p); break;
  case 189: code189(p); break;
  case 190: code190(p); break;
  case 191: code191(p); break;
  case 192: code192(p); break;
  case 193: code193(p); break;
  case 194: code194(p); break;
  case 195: code195(p); break;
  case 196: code196(p); break;
  case 197: code197(p); break;
  case 198: code198(p); break;
  case 199: code199(p); break;
  case 200: code200(p); break;
  case 201: code201(p); break;
  case 202: code202(p); break;
  case 203: code203(p); break;
  case 204: code204(p); break;
  case 205: code205(p); break;
  case 206: code206(p); break;
  case 207: code207(p); break;
  case 208: code208(p); break;
  case 209: code209(p); break;
  case 210: code210(p); break;
  case 211: code211(p); break;
  case 212: code212(p); break;
  case 213: code213(p); break;
  case 214: code214(p); break;
  case 215: code215(p); break;
  case 216: code216(p); break;
  case 217: code217(p); break;
  case 218: code218(p); break;
  case 219: code219(p); break;
  case 220: code220(p); break;
  case 221: code221(p); break;
  case 222: code222(p); break;
  case 223: code223(p); break;
  case 224: code224(p); break;
  case 225: code225(p); break;
  case 226: code226(p); break;
  case 227: code227(p); break;
  case 228: code228(p); break;
  case 229: code229(p); break;
  case 230: code230(p); break;
  case 231: code231(p); break;
  case 232: code232(p); break;
  case 233: code233(p); break;
  case 234: code234(p); break;
  case 235: code235(p); break;
  case 236: code236(p); break;
  case 237: code237(p); break;
  case 238: code238(p); break;
  case 239: code239(p); break;
  case 240: code240(p); break;
  case 241: code241(p); break;
  case 242: code242(p); break;
  case 243: code243(p); break;
  case 244: code244(p); break;
  case 245: code245(p); break;
  case 246: code246(p); break;
  case 247: code247(p); break;
  case 248: code248(p); break;
  case 249: code249(p); break;
  case 250: code250(p); break;
  case 251: code251(p); break;
  case 252: code252(p); break;
  case 253: code253(p); break;
  case 255: code255(p); break;
  case 268: code268(p); break;
  case 269: code269(p); break;
  case 270: code270(p); break;
  case 271: code271(p); break;
  case 272: code272(p); break;
  case 273: code273(p); break;
  case 274: code274(p); break;
  case 275: code275(p); break;
  case 276: code276(p); break;
  case 277: code277(p); break;
  case 278: code278(p); break;
  case 279: code279(p); break;
  case 280: code280(p); break;
  case 281: code281(p); break;
  case 282: code282(p); break;
  case 283: code283(p); break;
  case 284: code284(p); break;
  case 285: code285(p); break;
  case 286: code286(p); break;
  case 287: code287(p); break;
  case 288: code288(p); break;
  case 289: code289(p); break;
  case 290: code290(p); break;
  case 291: code291(p); break;
  case 292: code292(p); break;
  case 293: code293(p); break;
  case 294: code294(p); break;
  case 295: code295(p); break;
  case 296: code296(p); break;
  case 297: code297(p); break;
  case 298: code298(p); break;
  case 299: code299(p); break;
  case 300: code300(p); break;
  case 301: code301(p); break;
  case 302: code302(p); break;
  case 303: code303(p); break;
  case 304: code304(p); break;
  case 305: code305(p); break;
  case 306: code306(p); break;
  case 307: code307(p); break;
  case 308: code308(p); break;
  case 309: code309(p); break;
  case 310: code310(p); break;
  case 311: code311(p); break;
  case 312: code312(p); break;
  case 313: code313(p); break;
  case 314: code314(p); break;
  case 315: code315(p); break;
  case 316: code316(p); break;
  case 317: code317(p); break;
  case 318: code318(p); break;
  case 319: code319(p); break;
  case 320: code320(p); break;
  case 321: code321(p); break;
  case 322: code322(p); break;
  case 323: code323(p); break;
  case 324: code324(p); break;
  case 325: code325(p); break;
  case 326: code326(p); break;
  case 327: code327(p); break;
  case 328: code328(p); break;
  case 329: code329(p); break;
  case 330: code330(p); break;
  case 331: code331(p); break;
  case 332: code332(p); break;
  case 333: code333(p); break;
  case 334: code334(p); break;
  case 335: code335(p); break;
  case 336: code336(p); break;
  case 337: code337(p); break;
  case 338: code338(p); break;
  case 339: code339(p); break;
  case 340: code340(p); break;
  case 341: code341(p); break;
  case 342: code342(p); break;
  case 343: code343(p); break;
  case 344: code344(p); break;
  case 345: code345(p); break;
  case 346: code346(p); break;
  case 347: code347(p); break;
  case 348: code348(p); break;
  case 349: code349(p); break;
  case 350: code350(p); break;
  case 351: code351(p); break;
  case 352: code352(p); break;
  case 353: code353(p); break;
  case 354: code354(p); break;
  case 355: code355(p); break;
  case 356: code356(p); break;
  case 357: code357(p); break;
  case 358: code358(p); break;
  case 359: code359(p); break;
  case 360: code360(p); break;
  case 361: code361(p); break;
  case 362: code362(p); break;
  case 363: code363(p); break;
  case 364: code364(p); break;
  case 365: code365(p); break;
  case 366: code366(p); break;
  case 367: code367(p); break;
  case 368: code368(p); break;
  case 369: code369(p); break;
  case 370: code370(p); break;
  case 371: code371(p); break;
  case 372: code372(p); break;
  case 373: code373(p); break;
  case 374: code374(p); break;
  case 375: code375(p); break;
  case 376: code376(p); break;
  case 377: code377(p); break;
  case 378: code378(p); break;
  case 379: code379(p); break;
  case 380: code380(p); break;
  case 381: code381(p); break;
  case 382: code382(p); break;
  case 383: code383(p); break;
  case 384: code384(p); break;
  case 385: code385(p); break;
  case 386: code386(p); break;
  case 387: code387(p); break;
  case 388: code388(p); break;
  case 389: code389(p); break;
  case 390: code390(p); break;
  case 391: code391(p); break;
  case 392: code392(p); break;
  case 393: code393(p); break;
  case 394: code394(p); break;
  case 395: code395(p); break;
  case 396: code396(p); break;
  case 397: code397(p); break;
  case 398: code398(p); break;
  case 399: code399(p); break;
  case 400: code400(p); break;
  case 401: code401(p); break;
  case 402: code402(p); break;
  case 403: code403(p); break;
  case 404: code404(p); break;
  case 405: code405(p); break;
  case 406: code406(p); break;
  case 407: code407(p); break;
  case 408: code408(p); break;
  case 409: code409(p); break;
  case 410: code410(p); break;
  case 411: code411(p); break;
  case 412: code412(p); break;
  case 413: code413(p); break;
  case 414: code414(p); break;
  case 415: code415(p); break;
  case 416: code416(p); break;
  case 417: code417(p); break;
  case 418: code418(p); break;
  case 419: code419(p); break;
  case 420: code420(p); break;
  case 421: code421(p); break;
  case 422: code422(p); break;
  case 423: code423(p); break;
  case 424: code424(p); break;
  case 425: code425(p); break;
  case 426: code426(p); break;
  case 427: code427(p); break;
  case 428: code428(p); break;
  case 429: code429(p); break;
  case 430: code430(p); break;
  case 431: code431(p); break;
  case 432: code432(p); break;
  case 433: code433(p); break;
  case 434: code434(p); break;
  case 435: code435(p); break;
  case 436: code436(p); break;
  case 437: code437(p); break;
  case 438: code438(p); break;
  case 439: code439(p); break;
  case 440: code440(p); break;
  case 441: code441(p); break;
  case 442: code442(p); break;
  case 443: code443(p); break;
  case 444: code444(p); break;
  case 445: code445(p); break;
  case 447: code447(p); break;
  case 449: code449(p); break;
  case 451: code451(p); break;
  case 453: code453(p); break;
  case 454: code454(p); break;
  case 455: code455(p); break;
  case 456: code456(p); break;
  case 457: code457(p); break;
  case 458: code458(p); break;
  case 459: code459(p); break;
  case 460: code460(p); break;
  case 461: code461(p); break;
  case 462: code462(p); break;
  case 463: code463(p); break;
  case 464: code464(p); break;
  case 465: code465(p); break;
  case 466: code466(p); break;
  case 467: code467(p); break;
  case 468: code468(p); break;
  case 469: code469(p); break;
  case 470: code470(p); break;
  case 471: code471(p); break;
  case 472: code472(p); break;
  case 473: code473(p); break;
  case 474: code474(p); break;
  case 475: code475(p); break;
  case 476: code476(p); break;
  case 477: code477(p); break;
  case 478: code478(p); break;
  case 479: code479(p); break;
  case 480: code480(p); break;
  case 481: code481(p); break;
  case 482: code482(p); break;
  case 483: code483(p); break;
  case 484: code484(p); break;
  default:
    throw new OptimizingCompilerException("BURS","rule without emit code:",BURS_Debug.string[ruleno]);
  }
}
}
