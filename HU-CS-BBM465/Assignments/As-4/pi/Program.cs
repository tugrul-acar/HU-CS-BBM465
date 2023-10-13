using System;
using System.Drawing;
using System.IO;
using System.Data;
using Microsoft.VisualBasic.FileIO;
using Emgu.CV.ML;
using Emgu.CV;
using Emgu.CV.Structure;
using Accord.MachineLearning.DecisionTrees;
using Accord.MachineLearning.DecisionTrees.Learning;
using Accord.Statistics.Kernels;
using Accord.Statistics.Filters;
using System.Linq;

namespace pi
{
    class Program
    {
        static int numClasses = 15; // Number of classes in the dataset

        // Relative paths
        static string precomputed = "..\\pre-computed";
        static string train;
        static string val;
        static string data;
        static int[] times = new int[4];
        static double[] avg_Fpr = new double[4];
        static double[] avg_Tpr = new double[4];
        static double[] avg_F1 = new double[4];
        static void Main(string[] args)
        {
            data = args[1];
            train = data + "\\train";
            val = data + "\\val";

            if (args[3] == "precompute")
            {
                Console.WriteLine("Reading phishIRIS_DL_Dataset...");
                int fCounttrain = Directory.GetFiles(train, "*", System.IO.SearchOption.AllDirectories).Length;
                Console.WriteLine(fCounttrain + " images were found in train folder");
                int fCountval = Directory.GetFiles(val, "*", System.IO.SearchOption.AllDirectories).Length;
                Console.WriteLine(fCountval + "  images were found in val folder");
                int directoryCount = System.IO.Directory.GetDirectories(train).Length;
                Console.WriteLine(directoryCount + "  classes exist");
            }
            if (!Directory.Exists(precomputed))
            {
                Directory.CreateDirectory(precomputed);
            }
            if (!File.Exists($@"{precomputed}\\precomputed_CEDD_train.csv"))
            {
                Console.WriteLine("CEDD features are being extracted for train...");
                make_CEDD(train, $@"{precomputed}\\precomputed_CEDD_train.csv");
            }
            if (!File.Exists($@"{precomputed}\\precomputed_CEDD_val.csv"))
            {
                Console.WriteLine("CEDD features are being extracted for val...");
                make_CEDD(val, $@"{precomputed}\\precomputed_CEDD_val.csv");
            }
            if (!File.Exists($@"{precomputed}\\precomputed_FCTH_train.csv"))
            {
                Console.WriteLine("FCTH features are being extracted for train...");
                make_FCTH(train, $@"{precomputed}\\precomputed_FCTH_train.csv");
            }
            if (!File.Exists($@"{precomputed}\\precomputed_FCTH_val.csv"))
            {
                Console.WriteLine("FCTH features are being extracted for val...");
                make_FCTH(val, $@"{precomputed}\\precomputed_FCTH_val.csv");
            }
            if (!File.Exists($@"{precomputed}\\precomputed_SCD_train.csv"))
            {
                Console.WriteLine("SCD features are being extracted for train...");
                make_SCD(train, $@"{precomputed}\\precomputed_SCD_train.csv");
            }
            if (!File.Exists($@"{precomputed}\\precomputed_SCD_val.csv"))
            {
                Console.WriteLine("SCD features are being extracted for val...");
                make_SCD(val, $@"{precomputed}\\precomputed_SCD_val.csv");
            }
            if (args[3] == "trainval")
            {

                Console.WriteLine("Training with precomputed_FCTH_train.csv");
                make_ML($@"{precomputed}\\precomputed_FCTH_train.csv", $@"{precomputed}\\precomputed_FCTH_val.csv", 1);
                make_ML($@"{precomputed}\\precomputed_FCTH_train.csv", $@"{precomputed}\\precomputed_FCTH_val.csv", 0);
                make_ML($@"{precomputed}\\precomputed_FCTH_train.csv", $@"{precomputed}\\precomputed_FCTH_val.csv", 2);
                make_ML($@"{precomputed}\\precomputed_FCTH_train.csv", $@"{precomputed}\\precomputed_FCTH_val.csv", 3);

                int time = times[0] + times[1] + times[2] + times[3];
                Console.WriteLine("Done in " + time / 1000 + " seconds");
                Console.WriteLine("Testing with precomputed_FCTH_train.csv " + Directory.GetFiles(val, "*", System.IO.SearchOption.AllDirectories).Length + " samples");
                Console.WriteLine("Random Forest\t| TPR " + Math.Round(avg_Tpr[3], 3) + " | FPR " + Math.Round(avg_Fpr[3], 3) + " | F1 " + Math.Round(avg_F1[3], 3));
                Console.WriteLine("SVM\t\t| TPR " + Math.Round(avg_Tpr[0], 3) + " | FPR " + Math.Round(avg_Fpr[0], 3) + " | F1 " + Math.Round(avg_F1[0], 3));
                Console.WriteLine("Rtree\t\t| TPR " + Math.Round(avg_Tpr[1], 3) + " | FPR " + Math.Round(avg_Fpr[1], 3) + " | F1 " + Math.Round(avg_F1[1], 3));
                Console.WriteLine("Dtree\t\t| TPR " + Math.Round(avg_Tpr[2], 3) + " | FPR " + Math.Round(avg_Fpr[2], 3) + " | F1 " + Math.Round(avg_F1[2], 3));
                Console.WriteLine("--------------------------------------------------");


                Console.WriteLine("Training with precomputed_CEDD_train.csv");
                make_ML($@"{precomputed}\\precomputed_CEDD_train.csv", $@"{precomputed}\\precomputed_CEDD_val.csv", 1);
                make_ML($@"{precomputed}\\precomputed_CEDD_train.csv", $@"{precomputed}\\precomputed_CEDD_val.csv", 0);
                make_ML($@"{precomputed}\\precomputed_CEDD_train.csv", $@"{precomputed}\\precomputed_CEDD_val.csv", 2);
                make_ML($@"{precomputed}\\precomputed_CEDD_train.csv", $@"{precomputed}\\precomputed_CEDD_val.csv", 3);

                time = times[0] + times[1] + times[2] + times[3];
                Console.WriteLine("Done in " + time / 1000 + " seconds");
                Console.WriteLine("Testing with precomputed_CEDD_train.csv " + Directory.GetFiles(val, "*", System.IO.SearchOption.AllDirectories).Length + " samples");
                Console.WriteLine("Random Forest\t| TPR " + Math.Round(avg_Tpr[3], 3) + " | FPR " + Math.Round(avg_Fpr[3], 3) + " | F1 " + Math.Round(avg_F1[3], 3));
                Console.WriteLine("SVM\t\t| TPR " + Math.Round(avg_Tpr[0], 3) + " | FPR " + Math.Round(avg_Fpr[0], 3) + " | F1 " + Math.Round(avg_F1[0], 3));
                Console.WriteLine("Rtree\t\t| TPR " + Math.Round(avg_Tpr[1], 3) + " | FPR " + Math.Round(avg_Fpr[1], 3) + " | F1 " + Math.Round(avg_F1[1], 3));
                Console.WriteLine("Dtree\t\t| TPR " + Math.Round(avg_Tpr[2], 3) + " | FPR " + Math.Round(avg_Fpr[2], 3) + " | F1 " + Math.Round(avg_F1[2], 3));
                Console.WriteLine("--------------------------------------------------");


                Console.WriteLine("Training with precomputed_SCD_train.csv");
                make_ML($@"{precomputed}\\precomputed_SCD_train.csv", $@"{precomputed}\\precomputed_SCD_val.csv", 1);
                make_ML($@"{precomputed}\\precomputed_SCD_train.csv", $@"{precomputed}\\precomputed_SCD_val.csv", 0);
                make_ML($@"{precomputed}\\precomputed_SCD_train.csv", $@"{precomputed}\\precomputed_SCD_val.csv", 2);
                //make_ML($@"{precomputed}\\precomputed_SCD_train.csv", $@"{precomputed}\\precomputed_SCD_val.csv", 3);

                time = times[0] + times[1] + times[2] + times[3];
                Console.WriteLine("Done in " + time / 1000 + " seconds");
                Console.WriteLine("Testing with precomputed_SCD_train.csv " + Directory.GetFiles(val, "*", System.IO.SearchOption.AllDirectories).Length + " samples");
                //Console.WriteLine("Random Forest\t| TPR " + Math.Round(avg_Tpr[3], 3) + " | FPR " + Math.Round(avg_Fpr[3], 3) + " | F1 " + Math.Round(avg_F1[3], 3));
                Console.WriteLine("SVM\t\t| TPR " + Math.Round(avg_Tpr[0], 3) + " | FPR " + Math.Round(avg_Fpr[0], 3) + " | F1 " + Math.Round(avg_F1[0], 3));
                Console.WriteLine("Rtree\t\t| TPR " + Math.Round(avg_Tpr[1], 3) + " | FPR " + Math.Round(avg_Fpr[1], 3) + " | F1 " + Math.Round(avg_F1[1], 3));
                Console.WriteLine("Dtree\t\t| TPR " + Math.Round(avg_Tpr[2], 3) + " | FPR " + Math.Round(avg_Fpr[2], 3) + " | F1 " + Math.Round(avg_F1[2], 3));
                Console.WriteLine("--------------------------------------------------");

            }

        }


        public static void make_CEDD(string filePath, string csvPath)
        {
            var stopwatch = new System.Diagnostics.Stopwatch();
            stopwatch.Start();
            string[] myDirs = Directory.GetDirectories(filePath);
            string labels = "";

            for (int i = 1; i < 145; i++)
            {
                labels += "f" + i + ",";
            }
            labels += "label";

            using (System.IO.StreamWriter file_train = new System.IO.StreamWriter(csvPath, false))
            {
                file_train.WriteLine(labels);
            }

            int countLabel = 1;
            foreach (var myDir in myDirs)
            {
                string[] subFile = Directory.GetFiles(myDir);
                foreach (var myFile in subFile)
                {
                    double[] CEDDTable = new double[144];
                    Bitmap ImageData = new Bitmap(myFile);
                    CEDD_Descriptor.CEDD GetCEDD = new CEDD_Descriptor.CEDD();
                    CEDDTable = GetCEDD.Apply(ImageData);
                    write_csv(CEDDTable, csvPath, countLabel);
                }
                countLabel++;
            }

            string[] words = csvPath.Split('\\');
            stopwatch.Stop();
            var elapsedTime = stopwatch.ElapsedMilliseconds;
            Console.WriteLine("Done " + words[3] + " is regenerated in " + elapsedTime / 1000 + " seconds");

        }


        public static void make_FCTH(string filePath, string csvPath)

        {
            var stopwatch = new System.Diagnostics.Stopwatch();
            stopwatch.Start();
            string[] myDirs = Directory.GetDirectories(filePath);
            string labels = "";

            for (int i = 1; i < 193; i++)
            {
                labels += "f" + i + ",";
            }
            labels += "label";

            using (System.IO.StreamWriter file = new System.IO.StreamWriter(csvPath, false))
            {
                file.WriteLine(labels);
            }

            int countLabel = 1;
            foreach (var myDir in myDirs)
            {
                string[] subFile = Directory.GetFiles(myDir);
                foreach (var myFile in subFile)
                {
                    double[] FCTHTable = new double[192];
                    Bitmap ImageData = new Bitmap(myFile);
                    FCTH_Descriptor.FCTH GetFCTH = new FCTH_Descriptor.FCTH();
                    FCTHTable = GetFCTH.Apply(ImageData, 2);
                    write_csv(FCTHTable, csvPath, countLabel);
                }
                countLabel++;
            }
            string[] words = csvPath.Split('\\');
            stopwatch.Stop();
            var elapsedTime = stopwatch.ElapsedMilliseconds;

            Console.WriteLine("Done " + words[3] + " is regenerated in " + elapsedTime / 1000 + " seconds");
        }


        public static void make_SCD(string filePath, string csvPath)
        {

            var stopwatch = new System.Diagnostics.Stopwatch();
            stopwatch.Start();
            string[] myDirs = Directory.GetDirectories(filePath);
            string labels = "";
            for (int i = 1; i < 257; i++)
            {
                labels += "f" + i + ",";
            }
            labels += "label";
            using (System.IO.StreamWriter file_train = new System.IO.StreamWriter(csvPath, false))
            {
                file_train.WriteLine(labels);
            }
            int countLabel = 1;
            foreach (var myDir in myDirs)
            {
                string[] subFile = Directory.GetFiles(myDir);
                foreach (var myFile in subFile)
                {
                    Image<Bgr, Byte> image = new Image<Bgr, byte>(myFile);
                    Image<Lab, Byte> labImage = image.Convert<Lab, Byte>();
                    DenseHistogram hist = new DenseHistogram(256, new RangeF(0, 256));
                    hist.Calculate(new Image<Gray, byte>[] { labImage[0] }, false, null);
                    float[] histogram = hist.GetBinValues();
                    write_csv(histogram, csvPath, countLabel);
                }
                countLabel++;
            }
            string[] words = csvPath.Split('\\');
            stopwatch.Stop();
            var elapsedTime = stopwatch.ElapsedMilliseconds;

            Console.WriteLine("Done " + words[3] + " is regenerated in " + elapsedTime / 1000 + " seconds");
        }

        public static void make_ML(string train_filepath, string val_filepath, int algorithmMode)
        {
            // 1- Read and parse for train
            var stopwatch = new System.Diagnostics.Stopwatch();
            stopwatch.Start();
            DataTable trainDataTable = new DataTable();
            using (TextFieldParser parser = new TextFieldParser(train_filepath))
            {
                parser.TextFieldType = FieldType.Delimited;
                parser.SetDelimiters(",");
                bool columnsSet = false;
                while (!parser.EndOfData)
                {
                    string[] fields = parser.ReadFields();
                    if (!columnsSet)
                    {
                        // Set colummns
                        for (int i = 0; i < fields.Length; i++)
                        {
                            trainDataTable.Columns.Add(fields[i]);
                        }
                        columnsSet = !columnsSet;
                    }
                    else
                    {
                        // Set rows
                        trainDataTable.Rows.Add(fields);
                    }
                }
            }

            // Set up the training data
            Matrix<float> trainData = new Matrix<float>(trainDataTable.Rows.Count, trainDataTable.Columns.Count - 1);
            Matrix<int> trainLabels = new Matrix<int>(trainDataTable.Rows.Count, 1);
            for (int i = 0; i < trainDataTable.Rows.Count; i++)
            {
                for (int j = 0; j < trainDataTable.Columns.Count - 1; j++)
                {
                    trainData[i, j] = float.Parse(trainDataTable.Rows[i][j].ToString());
                }
                trainLabels[i, 0] = int.Parse(trainDataTable.Rows[i][trainDataTable.Columns.Count - 1].ToString());
            }

            // 2- Read and parse for validation
            DataTable valDataTable = new DataTable();
            using (TextFieldParser parser = new TextFieldParser(val_filepath))
            {
                parser.TextFieldType = FieldType.Delimited;
                parser.SetDelimiters(",");
                bool columnsSet = false;
                while (!parser.EndOfData)
                {
                    string[] fields = parser.ReadFields();
                    if (!columnsSet)
                    {
                        // Set colummns
                        for (int i = 0; i < fields.Length; i++)
                        {
                            valDataTable.Columns.Add(fields[i]);
                        }
                        columnsSet = !columnsSet;
                    }
                    else
                    {
                        // Set rows
                        valDataTable.Rows.Add(fields);
                    }
                }
            }


            // Set up the val data
            Matrix<float> valData = new Matrix<float>(valDataTable.Rows.Count, valDataTable.Columns.Count);
            Matrix<int> valLabels = new Matrix<int>(valDataTable.Rows.Count, 1);
            for (int i = 0; i < valDataTable.Rows.Count; i++)
            {
                for (int j = 0; j < valDataTable.Columns.Count; j++)
                {
                    valData[i, j] = float.Parse(valDataTable.Rows[i][j].ToString());
                }
                valLabels[i, 0] = int.Parse(valDataTable.Rows[i][valDataTable.Columns.Count - 1].ToString());
            }
            double[] testxd = new double[valDataTable.Columns.Count - 1];
            Matrix<float> testData = new Matrix<float>(1, valDataTable.Columns.Count - 1);
            Matrix<float> predictedLabels = new Matrix<float>(valDataTable.Rows.Count, 1);

            if (algorithmMode == 0) //SVM ML
            {

                // Set up the SVM model
                SVM model = new SVM();
                model.Type = SVM.SvmType.CSvc;
                model.SetKernel(SVM.SvmKernelType.Linear);

                TrainData trainDataObject = new TrainData(trainData, Emgu.CV.ML.MlEnum.DataLayoutType.RowSample, trainLabels);

                // Train the model

                model.Train(trainDataObject);

                // Test the model
                for (int j = 0; j < valDataTable.Rows.Count; j++)
                {
                    for (int i = 0; i < testData.Cols; i++)
                    {
                        testData[0, i] = float.Parse(valDataTable.Rows[j][i].ToString());
                    }
                    predictedLabels[j, 0] = model.Predict(testData);
                }

                stopwatch.Stop();
                var elapsedTime = stopwatch.ElapsedMilliseconds;
                times[0] = (int)elapsedTime;
            }


            else if (algorithmMode == 1)//Rtree
            {
                // Set up the Rtree model

                RTrees model = new RTrees();
                //model.MaxDepth = 20;
                /*model.MinSampleCount = 2;
                model.RegressionAccuracy = 0;
                model.UseSurrogates = false;
                model.MaxCategories = 15;
                model.CalculateVarImportance = true;
                model.ActiveVarCount = 10;*/
                TrainData trainDataObject = new TrainData(trainData, Emgu.CV.ML.MlEnum.DataLayoutType.RowSample, trainLabels);

                // Train the model
                model.Train(trainDataObject);

                // Test the model
                for (int j = 0; j < valDataTable.Rows.Count; j++)
                {
                    for (int i = 0; i < testData.Cols; i++)
                    {
                        testData[0, i] = float.Parse(valDataTable.Rows[j][i].ToString());
                    }
                    predictedLabels[j, 0] = model.Predict(testData);
                }

                stopwatch.Stop();

                var elapsedTime = stopwatch.ElapsedMilliseconds;
                times[1] = (int)elapsedTime;
            }

            else if (algorithmMode == 2) //Dtree
            {
                // Set up the Decision Trees model
                DTrees model = new DTrees();
                model.MaxDepth = 10;
                //model.MinSampleCount = 2;
                //model.RegressionAccuracy = 0;
                //model.UseSurrogates = false;
                //model.MaxCategories = 15;
                model.CVFolds = 0;

                // Train the model
                TrainData trainDataObject = new TrainData(trainData, Emgu.CV.ML.MlEnum.DataLayoutType.RowSample, trainLabels);
                model.Train(trainDataObject);

                // Test the model

                for (int j = 0; j < valDataTable.Rows.Count; j++)
                {
                    for (int i = 0; i < testData.Cols; i++)
                    {
                        testData[0, i] = float.Parse(valDataTable.Rows[j][i].ToString());
                    }
                    predictedLabels[j, 0] = model.Predict(testData);
                }
                stopwatch.Stop();


                var elapsedTime = stopwatch.ElapsedMilliseconds;
                times[2] = (int)elapsedTime;
            }

            else //RFT 
            {

                // Turning datas into arrays
                double[][] inputs = new double[trainDataTable.Rows.Count][];
                for (int i = 0; i < trainDataTable.Rows.Count; i++)
                {
                    inputs[i] = new double[trainDataTable.Columns.Count - 1];
                    for (int j = 0; j < trainDataTable.Columns.Count - 1; j++)
                    {
                        inputs[i][j] = double.Parse(trainDataTable.Rows[i][j].ToString());
                    }

                }
                for (int j = 0; j < trainDataTable.Columns.Count - 1; j++)
                {
                    inputs[5][j] = (double)1;
                }

                int[] outputs = new int[trainDataTable.Rows.Count];
                for (int i = 0; i < trainDataTable.Rows.Count; i++)
                {
                    outputs[i] = int.Parse(trainDataTable.Rows[i][trainDataTable.Columns.Count - 1].ToString());
                }

                // Random Forest Tree modelini oluştur ve eğit
                var teacher = new RandomForestLearning();
                var model = teacher.Learn(inputs, outputs);

                // Modeli kullanarak tahmin yap
                for (int j = 0; j < valDataTable.Rows.Count; j++)
                {
                    for (int i = 0; i < testData.Cols; i++)
                    {
                        testData[0, i] = float.Parse(valDataTable.Rows[j][i].ToString());
                        testxd[i] = double.Parse(valDataTable.Rows[j][i].ToString());

                    }
                    predictedLabels[j, 0] = model.Decide(testxd);
                }

                stopwatch.Stop();
                var elapsedTime = stopwatch.ElapsedMilliseconds;
                times[3] = (int)elapsedTime;

            }


            double[] fpr = new double[numClasses];
            double[] tpr = new double[numClasses];
            double[] f1 = new double[numClasses];
            int[] classCounts = new int[numClasses]; // Number of samples in each class

            for (int c = 0; c < numClasses; c++)
            {
                int truePositive = 0;
                int falsePositive = 0;
                int trueNegative = 0;
                int falseNegative = 0;
                for (int i = 0; i < valDataTable.Rows.Count; i++)
                {
                    if (predictedLabels[i, 0] == c + 1 && valLabels[i, 0] == c + 1)
                    {
                        truePositive++;
                    }
                    else if (predictedLabels[i, 0] == c + 1 && valLabels[i, 0] != c + 1)
                    {
                        falsePositive++;
                    }
                    else if (predictedLabels[i, 0] != c + 1 && valLabels[i, 0] != c + 1)
                    {
                        trueNegative++;
                    }
                    else if (predictedLabels[i, 0] != c + 1 && valLabels[i, 0] == c + 1)
                    {
                        falseNegative++;
                    }
                }

                double precision = (double)truePositive / (truePositive + falsePositive);
                double recall = (double)truePositive / (truePositive + falseNegative);
                fpr[c] = (double)falsePositive / (falsePositive + trueNegative);
                tpr[c] = (double)truePositive / (truePositive + falseNegative);
                if (truePositive == 0)
                {
                    f1[c] = 0;
                }
                else
                {
                    f1[c] = (2 * precision * recall) / (precision + recall);
                }

                classCounts[c] = truePositive + falseNegative;
            }

            // Calculate the weighted average FPR, TPR and F1 scores
            double avgFpr = 0.0;
            double avgTpr = 0.0;
            double avgF1 = 0.0;
            for (int c = 0; c < numClasses; c++)
            {
                avgFpr += fpr[c] * classCounts[c];
                avgTpr += tpr[c] * classCounts[c];
                avgF1 += f1[c] * classCounts[c];
            }
            avgFpr /= valDataTable.Rows.Count;
            avgTpr /= valDataTable.Rows.Count;
            avgF1 /= valDataTable.Rows.Count;
            avg_Fpr[algorithmMode] = avgFpr;
            avg_Tpr[algorithmMode] = avgTpr;
            avg_F1[algorithmMode] = avgF1;

        }


        public static void write_csv(double[] table, string filepath, int classname)
        {
            string result = "";

            for (int i = 0; i < table.Length; i++)
            {
                result += table[i] + ",";
            }
            result += classname;

            using (System.IO.StreamWriter file = new System.IO.StreamWriter(@filepath, true))
            {
                file.WriteLine(result);
            }
        }


        public static void write_csv(float[] table, string filepath, int classname)
        {
            string result = "";

            for (int i = 0; i < table.Length; i++)
            {
                result += table[i] + ",";
            }
            result += classname;

            using (System.IO.StreamWriter file = new System.IO.StreamWriter(@filepath, true))
            {
                file.WriteLine(result);
            }
        }
    }
}
