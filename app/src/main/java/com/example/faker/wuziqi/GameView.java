package com.example.faker.wuziqi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GameView extends View {

	/**
	 * 屏幕的宽
	 */
	private static int screenWidth = Screen.screenWidth;
	/**
	 * 屏幕的高
	 */
	private static int screenHeight = Screen.screenHeight;
	/**
	 * 棋盘的行数
	 */
	public static final int ROWS = 15;
	/**
	 * 棋盘的列数
	 */
	public static final int COLS = 15;
	/**
	 * 棋子在棋盘的分布0为无子，1为白子,2为黑子
	 */
	public int isChessOn [][];				//棋局
    protected int deep = 3,weight = 7;    // 搜索的深度以及广度
    public int sbw = 0;                          //玩家棋色黑色0，白色1
    public int bw=0;
  	// 边界值,用于速度优化
    protected int x_max = 15, x_min = 0;
    protected int y_max = 15, y_min = 0;
 	public int BLACK_ONE;					//0表黑子
 	public int WHITE_ONE;					//1表白子
 	public int NONE_ONE;					//2表无子
 	public boolean first=true;
	private static float PADDING = ((float) (screenWidth) / (COLS - 1)) / 2;
	private static float PADDING_LEFT = ((float) (screenWidth) / (COLS - 1)) / 2;
	private static float PADDING_TOP = ((float) (screenHeight) / (ROWS - 1)) / 2;
	private static float ROW_MARGIN = ((float) (screenHeight - PADDING * 2))
			/ (ROWS - 1);
	private static float COL_MARGIN = ((float) (screenWidth - PADDING * 2))
			/ (COLS - 1);
	private static float MARGIN;
	// 判断游戏是否结束
	private boolean gameOver = false;
	// 主activity
	private Context context = null;




	public GameView(Context context) {
		super(context);
		this.context = context;
		this.setBackgroundResource(R.drawable.bg);
		initChess();
		PADDING_LEFT = ((screenWidth) / (COLS - 1)) / 2;
		PADDING_TOP = ((screenHeight) / (ROWS - 1)) / 2;
		PADDING = PADDING_LEFT < PADDING_TOP ? PADDING_LEFT : PADDING_TOP;
		ROW_MARGIN = ((screenHeight - PADDING * 2)) / (ROWS - 1);
		COL_MARGIN = ((screenWidth - PADDING * 2)) / (COLS - 1);
		MARGIN = ROW_MARGIN < COL_MARGIN ? ROW_MARGIN : COL_MARGIN;
		PADDING_LEFT = (screenWidth - (COLS - 1) * MARGIN) / 2;
		PADDING_TOP = (screenHeight - (ROWS - 1) * MARGIN) / 2;
		// 对棋子进行初始化

		// System.out.println(PADDING_LEFT + " " + PADDING_TOP);
	}

	/**
	 * 对棋子进行初始化
	 */
	public void initChess() {
  		NONE_ONE=2;
  		BLACK_ONE=0;
  		WHITE_ONE=1;
  		bw=0;
   		isChessOn=new int[ROWS][COLS];
   		for(int i=0;i<ROWS;i++)
   			for(int j=0;j<COLS;j++){
   				isChessOn[i][j]=NONE_ONE;
   			}
   		x_max = 15; x_min = 0; // 边界值,用于速度优化
   	    y_max = 15;y_min = 0;
	}
	/**
	 * 游戏重新开始
	 */
	public void reStart(){
		initChess();
		invalidate();
		gameOver = false;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		// 打印行
		for (int i = 0; i < ROWS; i++) {
			canvas.drawLine(PADDING_LEFT, i * MARGIN + PADDING_TOP, (COLS - 1)
					* MARGIN + PADDING_LEFT, i * MARGIN + PADDING_TOP, paint);
		}
		// 打印列
		for (int i = 0; i < COLS; i++) {
			canvas.drawLine(PADDING_LEFT + i * MARGIN, PADDING_TOP,
					PADDING_LEFT + i * MARGIN, MARGIN * (ROWS - 1)
							+ PADDING_TOP, paint);
		}
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				// System.out.print(chessMap[r][c] + " ");
				if (isChessOn[r][c] == NONE_ONE)
					continue;
				if (isChessOn[r][c] == BLACK_ONE) {
					paint.setColor(Color.BLACK);
					canvas.drawCircle(r * MARGIN + PADDING_LEFT, c * MARGIN
							+ PADDING_TOP, MARGIN / 2, paint);
				} else if (isChessOn[r][c] == WHITE_ONE) {
					paint.setColor(Color.WHITE);
					canvas.drawCircle(r * MARGIN + PADDING_LEFT, c * MARGIN
							+ PADDING_TOP, MARGIN / 2, paint);
				}
			}
		}
	}

	/**
	 * 判断是否胜利
	 */
	public boolean hasWin(int r, int c) {
		int chessType = isChessOn[r][c];
		System.out.println(chessType);
		int count = 1;
		// 纵向搜索
		for (int i = r + 1; i < r + 5; i++) {
			if (i >= GameView.ROWS)
				break;
			if (isChessOn[i][c] == chessType) {
				count++;
			} else
				break;
		}
		for (int i = r - 1; i > r - 5; i--) {
			if (i < 0)
				break;
			if (isChessOn[i][c] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" "+"1");
		if (count >= 5)
			return true;
		// 横向搜索
		count = 1;
		for (int i = c + 1; i < c + 5; i++) {
			if (i >= GameView.COLS)
				break;
			if (isChessOn[r][i] == chessType)
				count++;
			else
				break;
		}
		for (int i = c - 1; i > c - 5; i--) {
			if (i < 0)
				break;
			if (isChessOn[r][i] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" " +"2");
		if (count >= 5)
			return true;
		// 斜向"\"
		count = 1;
		for (int i = r + 1, j = c + 1; i < r + 5; i++, j++) {
			if (i >= GameView.ROWS || j >= GameView.COLS) {
				break;
			}
			if (isChessOn[i][j] == chessType)
				count++;
			else
				break;
		}
		for (int i = r - 1, j = c - 1; i > r - 5; i--, j--) {
			if (i < 0 || j < 0)
				break;
			if (isChessOn[i][j] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" " +"3");
		if (count >= 5)
			return true;
		// 斜向"/"
		count = 1;
		for (int i = r + 1, j = c - 1; i < r + 5; i++, j--) {
			if (i >= GameView.ROWS || j < 0)
				break;
			if (isChessOn[i][j] == chessType)
				count++;
			else
				break;
		}
		for (int i = r - 1, j = c + 1; i > r - 5; i--, j++) {
			if (i < 0 || j >= GameView.COLS)
				break;
			if (isChessOn[i][j] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" " +"4");
		if (count >= 5)
			return true;
		return false;
	}

	public void change()
	{
		sbw=1-sbw;
		initChess();
		if(1==sbw)//玩家为白后手
		{
			isChessOn[7][7]=1-sbw; //电脑先手下（7，7）

		}
		bw=sbw;//黑棋下完玩家下白棋 ,玩家先手下黑棋
		invalidate();
		gameOver = false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX(); //获取X坐标
		float y = event.getY(); //获取Y坐标
		int r = Math.round((x - this.PADDING_LEFT) / this.MARGIN);
		int c = Math.round((y - this.PADDING_TOP) / this.MARGIN);
		// System.out.println(r + " " + c);

		if (!(r >= 0 && r < ROWS && c >= 0 && c < COLS)) {
			return false;
		}
		if (!gameOver) {//游戏还没结束
			if (isChessOn[r][c] == NONE_ONE) {
				isChessOn[r][c] = bw;
				this.invalidate();
				bw=1-bw;
				if (this.hasWin(r, c)) {
					// 玩家胜利
					this.gameOver = true;
					new AlertDialog.Builder(context).setTitle("Congratulations")
							.setMessage(" You win").setPositiveButton("OK", null).show();
					return super.onTouchEvent(event);
				}
				if(first)
				{
					if(r-1>=0)
					{
						x_min = r-1;
					}
					if(r-1<=15)
        	  		{
						x_max = r+1;
        	  		}
					if(c-1>=0)
					{
						y_min = c-1;
					}
					if(c-1<=15)
					{
						y_max = c+1;
					}
					first=false;
				}
				else
				{
					resetMaxMin(r,c);
				}
        	Point p=putOne(bw);
			isChessOn[p.x][p.y] = bw;//电脑下个棋
			this.invalidate();
			bw=1-bw;
				if (this.hasWin(p.x, p.y)) {
					// 电脑胜利
					this.gameOver = true;
					new AlertDialog.Builder(context).setTitle("Unfortunately")
							.setMessage("You loss").setPositiveButton("OK", null)
							.show();
				}
			}
		} else {
			new AlertDialog.Builder(context)
				.setTitle("Tip")
				.setMessage("Game over ! Reopen a new game?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						reStart();
					}
				}).setNegativeButton("Cancel", null).show();
			return super.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

   //----------------------电脑下棋-------------------------------//
  public Point putOne(int bwf ) {  //bwf 棋色 0:黑色 1：白色
      int x, y, mx = -1000000000;
      x = y = -1;
      // 搜索最优下棋点
      int[][] bests = getBests( bwf );
      for (int k = 0; k < bests.length; k++) {
          int i = bests[k][0];
          int j = bests[k][1];
          // 有成5,则直接下子,并退出循环..没有,则思考对方情况
          if (getType(i, j, bwf) == 1) {
              x = i;
              y = j;
              break;
          }
          if (getType(i, j,1 - bwf) == 1) {
              x = i;
              y = j;
              break;
          }
          // 预存当前边界值
          int temp1=x_min,temp2=x_max,temp3=y_min,temp4=y_max;
          // 预设己方下棋,并更新边界值
          isChessOn[i][j] = bwf;
          resetMaxMin(i,j);
          // 预测未来
          int t = findMin(-100000000, 100000000, deep);
          // 还原预设下棋位置以及边界值
          isChessOn[i][j] = 2;
          x_min=temp1;
          x_max=temp2;
          y_min=temp3;
          y_max=temp4;
          // 差距小于1000，50%概率随机选取
          //System.out.println("外       :" + i + "," + j + "  mx:" + mx + "  t:" + t);
          if (t - mx > 1000 || Math.abs(t - mx)<1000 && randomTest(3)) {
              x = i;
              y = j;
              mx = t;
          }
          //System.out.println(i + "," + j + "  mx:" + mx + "  t:" + t);
      }
       //记录
      System.out.println("[" + x + "," + y + "]");
      Point p =new Point();
      p.set(x, y);
      // 重设边界值
      resetMaxMin(x,y);
      return p;
  }

  //---------搜索当前搜索状态极大值--------------------------------//
  //alpha 祖先节点得到的当前最小最大值，用于alpha 剪枝
  //beta  祖先节点得到的当前最大最小值，用于beta 剪枝。
  //step  还要搜索的步数
  //return 当前搜索子树极大值
  protected int findMax(int alpha, int beta, int step) {
  	int max = alpha;
      if (step == 0) {
          return evaluate();
      }
      int[][] rt = getBests(1 - sbw);
      for (int i = 0; i < rt.length; i++) {
          int x = rt[i][0];
      	int y = rt[i][1];
      	if (getType(x, y, 1 - sbw) == 1)   //电脑可取胜
      		return 100 * ( getMark(1) + step*1000 );
          isChessOn[x][y] = 1 - sbw;
          // 预存当前边界值
          int temp1=x_min,temp2=x_max,temp3=y_min,temp4=y_max;
          resetMaxMin(x,y);
          int t = findMin(max, beta, step - 1);
          isChessOn[x][y] = 2;
          // 还原预设边界值
          x_min=temp1;
          x_max=temp2;
          y_min=temp3;
          y_max=temp4;
          if (t > max)
          	max = t;
          //beta 剪枝
          if (max >= beta)
              return max;
      }
      return max;
  }


   //-----------------------搜索当前搜索状态极小值---------------------------------//
   //alpha 祖先节点得到的当前最小最大值，用于alpha 剪枝
  //beta  祖先节点得到的当前最大最小值，用于beta 剪枝
  //step  还要搜索的步数
 //return 当前搜索子树极小值。
  protected int findMin(int alpha, int beta, int step) {
  	int min = beta;
      if (step == 0) {
          return evaluate();
      }
      int[][] rt = getBests(sbw);
      for (int i = 0; i < rt.length; i++) {
          int x = rt[i][0];
          int y = rt[i][1];
          int type = getType(x, y, sbw);
          if (type == 1)     					  			//玩家成5
              return -120 * ( getMark(1) + step*1000 );
          // 预存当前边界值
          int temp1=x_min,temp2=x_max,temp3=y_min,temp4=y_max;
          isChessOn[x][y] = sbw;
          resetMaxMin(x,y);
          int t = findMax( alpha, min, step - 1 );
          isChessOn[x][y] = 2;
          // 还原预设边界值
          x_min=temp1;
          x_max=temp2;
          y_min=temp3;
          y_max=temp4;
          if (t < min)
          	min = t;
          //alpha 剪枝
          if (min <= alpha) {
              return min;
          }
      }
      return min;
  }


   //-----------------选取局部最优的几个落子点作为下一次扩展的节点---------//
   //bwf 棋色 0：黑棋 1：白棋
   //return 选出来的节点坐标
  private int[][] getBests(int bwf) {
      int i_min=(x_min==0 ? x_min:x_min-1);
      int j_min=(y_min==0 ? y_min:y_min-1);
      int i_max=(x_max==15 ? x_max:x_max+1);
      int j_max=(y_max==15 ? y_max:y_max+1);
      int n = 0;
      int type_1,type_2;
      int[][] rt = new int[(i_max-i_min) * (j_max-j_min)][3];
      for ( int i = i_min; i < i_max; i++)
      	for (int j = j_min; j < j_max; j++)
      		if (isChessOn[i][j] == 2) {
                  type_1 = getType(i, j, bwf);
                  type_2 = getType(i, j, 1 - bwf);
                  rt[n][0] = i;
                  rt[n][1] = j;
                  rt[n][2] = getMark(type_1) + getMark(type_2);
                  n++;
      }
      // 对二维数组排序
      Arrays.sort(rt, new ArrComparator());
      int size = weight > n? n:weight;
      int[][] bests = new int[size][3];
      System.arraycopy(rt, 0, bests, 0, size);
      return bests;
  }

   //----------------------------计算指定方位上的棋型-------------------//
   // x,y 方向线基准一点。
   //ex,ey 指定方向步进向量。
   // bwf 棋子颜色，0：黑色，1：白色
   // return 该方向上的棋子数目 以及 活度

   private int[] count(int x, int y, int ex, int ey, int bwf) {
  	// 该方向没意义,返回0
      if( !makesense(x, y, ex, ey, bwf))//makesense()为判断是否大于5
          return new int[] {0, 1};

      // 正方向 以及 反方向棋子个数
  	int rt_1 = 1,rt_2 = 1;
  	// 总棋子个数
  	int rt = 1;
  	// 正方向 以及 反方向连子的活度
      int ok_1 = 0,ok_2 =0;
      // 总活度
      int ok = 0;
      // 连子中间有无空格
      boolean flag_mid1 =false,flag_mid2 = false;
      // 连子中间空格的位置
      int flag_i1 = 1,flag_i2 = 1;

      if (isChessOn[x][y] != 2) {
          throw new IllegalArgumentException("position x,y must be empty!..");
      }
      int i;
      // 往正方向搜索
      for (i = 1; x + i * ex < 15 && x + i * ex >= 0 && y + i * ey < 15 && y + i * ey >= 0; i++) {
          if (isChessOn[x + i * ex][y + i * ey] == bwf)
              rt_1++;
      	// 位置为空,若中空标志为false,则记为中空并继续搜索  否则,break
          else if(isChessOn[x + i * ex][y + i * ey] == 2) {
          		if(!flag_mid1) {
          			flag_mid1 = true;
          			flag_i1 = i;
          		}
          		else
          			break;
          	}
          // 位置为对方棋子
          else
          	break;
      }
      // 计算正方向活度,,
      // 最后一个位置不超过边界
      if (x + i * ex < 15 && x + i * ex >= 0 && y + i * ey < 15 && y + i * ey >= 0) {
      	// 最后一个位置为空位 +1活
      	if( isChessOn[x + i * ex][y + i * ey] == 2) {
      		ok_1++;
      		// 若是在尾部检测到连续的空格而退出搜索,则不算有中空
              if(rt_1 == flag_i1)
      			flag_mid1 = false;
              // 若中空的位置在4以下 且 棋子数>=4,则这一边的4非活
              if(flag_mid1 && rt_1 > 3 && flag_i1 < 4) {
              	ok_1--;
              }
      	}
      	// 最后一个位置不是空格,且搜索了2步以上,若前一个是空格,  则不算中空,且为活的边
      	else if( isChessOn[x + i * ex][y + i * ey] != bwf && i >= 2)
          	if(isChessOn[x + (i-1) * ex][y + (i-1) * ey] == 2) {
          		ok_1++;
          		flag_mid1 = false;
          	}
      }
      // 最后一个位置是边界  搜索了2步以上,且前一个是空格,  则不算中空,且为活的边
      else if(i >= 2 && isChessOn[x + (i-1) * ex][y + (i-1) * ey] == 2) {
      	ok_1++;
      	flag_mid1 = false;
      }

      // 往反方向搜索
      for (i = 1; x - i * ex >= 0 && x - i * ex < 15 && y - i * ey >= 0 && y - i * ey < 15; i++) {
          if (isChessOn[x - i * ex][y - i * ey] == bwf)
              rt_2++;
          else if(isChessOn[x - i * ex][y - i * ey] == 2) {
          		if(!flag_mid2) {
          			flag_mid2 = true;
          			flag_i2 = i;
          		}
          		else
          			break;
          	}
          else
              break;
      }
      // 计算反方向活度
      if (x - i * ex < 15 && x - i * ex >= 0 && y - i * ey < 15 && y - i * ey >= 0) {
      	if( isChessOn[x - i * ex][y - i * ey] == 2) {
      		ok_2++;
      		if(rt_2 == flag_i2)
      			flag_mid2 = false;
      	    if(flag_mid2 && rt_2 > 3 && flag_i2 < 4) {
              	ok_2--;
              }
      	}
      	else if( isChessOn[x - i * ex][y - i * ey] != bwf && i >= 2 )
      		if(isChessOn[x - (i-1) * ex][y - (i-1) * ey] == 2) {
      			ok_2++;
      			flag_mid2 = false;
      		}
      }
      else if(i >= 2 && isChessOn[x - (i-1) * ex][y - (i-1) * ey] == 2) {
      	ok_2++;
  		flag_mid2 = false;
      }

      //------------------分析棋子类型
      // 两边都没中空,直接合成
      if( !flag_mid1 && !flag_mid2 ) {
      	rt = rt_1 + rt_2 - 1;
      	ok = ok_1 + ok_2;
      	return new int[] {rt, ok};
      }
      // 两边都有中空
      else if( flag_mid1 && flag_mid2 ){
      	int temp = flag_i1 + flag_i2 - 1;
      	// 判断中间的纯连子数,在5以上,直接返回;  为4,返回活4;
      	if(temp >= 5)
      		return new int[] {temp, 2};
      	if(temp == 4)
      		return new int[] {temp, 2};
      	// 先看有没死4,再看有没活3,剩下只能是死3
      	if(rt_1 + flag_i2 - 1 >= 4 || rt_2 + flag_i1 - 1 >= 4)
      		return new int[] {4, 1};
      	if(rt_1+flag_i2-1 == 3 && ok_1 > 0 || rt_2+flag_i1-1 == 3 && ok_2 > 0)
      		return new int[] {3, 2};
      	return new int[] {3, 1};
      }
      // 有一边有中空
      else {
      	// 总棋子数少于5,直接合成
      	if( rt_1 + rt_2 - 1 < 5 )
      		return new int[] {rt_1 + rt_2 - 1, ok_1 + ok_2};
      	// 多于5,先找成5,再找活4,剩下的只能是死4
      	else {
      		if(flag_mid1 && rt_2 + flag_i1 - 1 >= 5)
      			return new int[] {rt_2 + flag_i1 - 1, ok_2 + 1};
      		if(flag_mid2 && rt_1 + flag_i2 - 1 >= 5)
      			return new int[] {rt_1 + flag_i2 - 1, ok_1 + 1};

      		if(flag_mid1 && (rt_2 + flag_i1 - 1 == 4 && ok_2 == 1 || flag_i1 == 4) )
      			return new int[] {4, 2};
      		if(flag_mid2 && (rt_1 + flag_i2 - 1 == 4 && ok_1 == 1 || flag_i2 == 4) )
      			return new int[] {4, 2};

      		return new int[] {4, 1};
      	}
      }
  }


   //----------------------------判断指定方向下棋是否有意义,即最大可能的棋子数是否 >=5-------------------------------//
   // x,y 评估的基准点
   // ex,ey 方向向量
   // bwf 棋色
   // 返回 true:有意义 false:没意义  评估棋子向量
private Boolean makesense(int x, int y, int ex, int ey, int bwf) {

      int rt = 1;
      for (int i = 1; x + i * ex < 15 && x + i * ex >= 0 && y + i * ey < 15 && y + i * ey >= 0 && rt < 5; i++)
          if (isChessOn[x + i * ex][y + i * ey] != 1 - bwf)
              rt++;
          else
              break;

      for (int i = 1; x - i * ex >= 0 && x - i * ex < 15 && y - i * ey >= 0 && y - i * ey < 15 && rt < 5; i++)
          if (isChessOn[x - i * ex][y - i * ey] != 1 - bwf)
              rt++;
          else
              break;
      return (rt >= 5);
  }


   //------------------------------------ 棋型判别-------------------------------------//
   // x,y 落子位置
   // bwf 棋色  0：黑子，1：白子
   // 对应的棋型： 棋型代码对应如下：
   //             1：成5
   //             2：成活4或者是双死4或者是死4活3
   //             3：成双活3
   //             4：成死3活3
   //             5：成死4
   //             6：单活3
   //             7：成双活2
  //             8：成死3
   //            9：成死2活2
   //            10：成活2
   //             11：成死2
   //             12: 其他

 protected int getType(int x, int y, int bwf) {
  	if (isChessOn[x][y] != 2)
          return -1;
  	  int[][] types = new int[4][2];
	  types[0] = count(x, y, 0, 1, bwf);   // 竖直
      types[1] = count(x, y, 1, 0, bwf);   // 横向
      types[2] = count(x, y, -1, 1, bwf);  // 斜上
      types[3] = count(x, y, 1, 1, bwf);   // 斜下
      // 各种棋型的方向的数目
      int longfive = 0;
      int five_OR_more = 0;
      int four_died = 0, four_live = 0;
      int three_died = 0, three_live = 0;
      int two_died  = 0, two_live = 0;
      // 各方向上棋型的判别
      for (int k = 0; k < 4; k++) {
      	if (types[k][0] > 5) {
      		longfive++;              // 长连
      		five_OR_more++;
      	}
      	else if (types[k][0] == 5)
      		five_OR_more++;          // 成5
          else if (types[k][0] == 4 && types[k][1] == 2)
          	four_live++;             // 活4
          else if (types[k][0] == 4 && types[k][1] != 2)
          	four_died++;             // 死4
          else if (types[k][0] == 3 && types[k][1] == 2)
          	three_live ++;           // 活3
          else if (types[k][0] == 3 && types[k][1] != 2)
          	three_died++;            // 死3
          else if (types[k][0] == 2 && types[k][1] == 2)
          	two_live++;              // 活2
          else if (types[k][0] == 2 && types[k][1] != 2)
          	two_died++;              // 死2
          else
              ;
      }
      if (five_OR_more != 0)
          return 1;   // 成5
      if (four_live != 0 || four_died >= 2 || four_died != 0 && three_live  != 0)
          return 2;   // 成活4或者是双死4或者是死4活3
      if (three_live  >= 2)
          return 3;   // 成双活3
      if (three_died != 0 && three_live  != 0)
          return 4;   // 成死3活3
      if (four_died != 0)
          return 5;   // 成死4
      if (three_live  != 0)
          return 6;   // 单活3
      if (two_live >= 2)
          return 7;   // 成双活2
      if (three_died != 0)
          return 8;   // 成死3
      if (two_live != 0 && two_died != 0)
          return 9;   // 成死2活2
      if (two_live != 0)
          return 10;  // 成活2
      if (two_died != 0)
          return 11;  // 成死2
      return 12;
  }
   //--------------------------对当前棋面进行打分------------------------------------------------------------//
  	//返回分值
    protected int evaluate() {
  	int rt = 0, mt_c = 1, mt_m = 1;
  	if(bw== sbw)
  		mt_m = 2;
  	else
  		mt_c = 2;
  	int i_min=(x_min==0 ? x_min:x_min-1);
      int j_min=(y_min==0 ? y_min:y_min-1);
      int i_max=(x_max==15 ? x_max:x_max+1);
      int j_max=(y_max==15 ? y_max:y_max+1);
      for (int i = i_min; i < i_max; i++)
          for (int j = j_min; j < j_max; j++)
              if (isChessOn[i][j] == 2) {
              	// 电脑棋面分数
                  int type = getType(i, j, 1 - sbw );
                  if(type == 1)      // 棋型1,棋型2以及棋型3,加权.  防止"4个双活3"的局分大于"1个双四"之类的错误出现
                  	 rt += 30 * mt_c * getMark(type);

                  else if(type == 2)
                  	rt += 10 * mt_c * getMark(type);
                  else if(type == 3)
                  	rt += 3 * mt_c * getMark(type);
                  else
                  	rt += mt_c * getMark(type);
                  // 玩家棋面分数
                  type = getType(i, j, sbw );
                  if(type == 1)
                  	rt -= 30 * mt_m * getMark(type);
                  else if(type == 2)
                  	rt -= 10 * mt_m * getMark(type);
                  else if(type == 3)
                  	rt -= 3 * mt_m * getMark(type);
                  else
                  	rt -= mt_m * getMark(type);
              }
      return rt;
  }


   //-------------------------------------- 下棋后,重设边界值------------------------------//
   // x 当前下棋位置的x坐标
   // y 当前下棋位置的y坐标

   public void resetMaxMin(int x,int y){
		if(x-1>=0)
      	x_min = (x_min<x-1 ? x_min:x-1);
      if(x+1<=15)
      	x_max = (x_max>x+1 ? x_max:x+1);
      if(y-1>=0)
      	y_min = (y_min<y-1 ? y_min:y-1);
      if(y+1<=15)
      	y_max = (y_max>y+1 ? y_max:y+1);
  }


   //------------------------------------------对分数相同的落子点，随机选取-------------------//
   //   kt 随机因子 值越小，被选取的概率越大
   //  return 是否选择该位置

  private boolean randomTest(int kt) {
      Random rm = new Random();
      return rm.nextInt() % kt == 0;
  }


   //------------------------------------- 不同棋型对应分数---------------------------------
   // k 棋型代号
   //return 对应分数
  private int getMark(int k) {
      switch (k) {
      case 1:
          return 100000;
      case 2:
          return 30000;
      case 3:
          return 5000;
      case 4:
          return 1000;
      case 5:
          return 500;
      case 6:
          return 200;
      case 7:
          return 100;
      case 8:
          return 50;
      case 9:
          return 10;
      case 10:
          return 5;
      case 11:
          return 3;
      case 12:
       	  return 2;
      default:
          return 0;
      }
  }
}

/**
 * 排序 Comparator
 */
class ArrComparator implements Comparator<Object> {
    int column = 2;

    int sortOrder = -1; // 递减

    public ArrComparator() {
    }
    public int compare(Object a, Object b) {
        if (a instanceof int[]) {
            return sortOrder * (((int[]) a)[column] - ((int[]) b)[column]);
        }
        throw new IllegalArgumentException("param a,b must int[].");
    }

}
