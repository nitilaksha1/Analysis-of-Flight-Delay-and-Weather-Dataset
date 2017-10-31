import numpy as np
from sklearn.cross_validation import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import Imputer
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.model_selection import KFold
from sklearn.model_selection import cross_val_score
from pandas import read_csv
import sys

# Function to check the features for missing values and replace them with Nan
def checkMissingValuesForField(dataset, metric, columnnumber):
    print(dataset.loc[:,columnnumber].describe())

    if metric == "wind":
        (dataset.loc[:,columnnumber]).replace(9999, np.NaN, inplace=True)

    if metric == "cig":
        (dataset.loc[:,columnnumber]).replace(99999, np.NaN, inplace=True)

    if metric == "visibility":        
        (dataset.loc[:,columnnumber]).replace(999999, np.NaN, inplace=True)

    if metric == "airtemperature":        
        (dataset.loc[:,columnnumber]).replace(9999, np.NaN, inplace=True)

    if metric == "dew":        
        (dataset.loc[:,columnnumber]).replace(9999, np.NaN, inplace=True)

    if metric == "slp":        
        (dataset.loc[:,columnnumber]).replace(99999, np.NaN, inplace=True)


def flightPredictor():
    # Getting the command line parameters
    num_split = int(sys.argv[1])
    train_percent = sys.argv[2].split(',')
    filename = sys.argv[3]
    
    # load data from csv files
    #train = np.loadtxt(filename, delimiter=',')
    dataset = read_csv(filename, header=None)

    #Checking data statistics for analysis
    print("DATASET STATISTICS")
    print(dataset.describe())
    
    checkMissingValuesForField(dataset, "wind", )
    checkMissingValuesForField(dataset, "cig", )
    checkMissingValuesForField(dataset, "visibility", )
    checkMissingValuesForField(dataset, "dew", )
    checkMissingValuesForField(dataset, "slp", )

    values = dataset.values

    print("DATASET WITH NAN VALUES")

    imputer = Imputer()
    transformed_values = imputer.fit_transform(values)
    
    # count the number of NaN values in each column
    print(np.isnan(transformed_values).sum())

    X = transformed_values[:,1:]
    Y = transformed_values[:, 0]

    print(X.shape)
    print(Y.shape)

    # testing if missing values are removed
    model = LinearDiscriminantAnalysis()
    kfold = KFold(n_splits=3, random_state=7)
    result = cross_val_score(model, X, Y, cv=kfold, scoring='accuracy')
    print(result.mean())

    classifier("RandomForest", "", num_split, train_percent, X, Y)

# This function starts the classification process which follows the missing data handling
def classifier(method_name, filename, num_split, train_percent, X, Y):    
    data_split_and_predict(method_name, "Weather-FlightData", X, Y, train_percent, num_split)


# This function calculates the test set error
def calc_error(predictedY, y_test):
    k = 0
    errors = 0

    while k < len(predictedY):
        if predictedY[k] != y_test[k]:
            errors = errors + 1

        k = k + 1

    errorrate = float(errors) / len(predictedY)
    return errorrate

# This function trains the model using 80:20 trainsplit and predicts based on the method specified
def data_split_and_predict (method_name, dataset_name, datasetX, datasetY, train_percent, num_split):
    i = 0
    mean_all_runs = list()
    trainpercentmean = {}

    for l in range(len(train_percent)):
        trainpercentmean[int(train_percent[l])] = 0

    print ("Printing statistics for {}".format(dataset_name))

    # Performing 80-20 train test split
    while i < num_split:
        X_train, X_test, y_train, y_test = train_test_split(datasetX, datasetY, test_size=0.2)
        j = 0

        mean_per_run_error = list()

        while (j < len(train_percent)):
            start_index = 0
            train_rate = int(train_percent[j])
            end_index = int((float(train_rate) / 100) * len(X_train))

            miniXslice = X_train[start_index:end_index, 0:]
            miniYslice = y_train[start_index:end_index]

            if method_name == "LogisticRegression":

                #Large Dataset setting for LR and also standardizing the features
                scaler = StandardScaler()
                miniXslice = scaler.fit_transform(miniXslice)

                # Create logistic regression object using stochastic gradient descent solver
                myLR = LogisticRegression(random_state=0, solver='sag')

                # Train model
                myLR.fit(miniXslice, miniYslice)

                #predict
                predictedY = myLR.predict(X_test)

            if method_name == "RandomForest":
                rf = RandomForestRegressor()
                rf.fit(miniXslice, miniYslice)
                predictedY = rf.predict(X_test)

            # calculating per run error
            errorrate = calc_error(predictedY, y_test)
            trainpercentmean[int(train_percent[j])] += errorrate
            mean_per_run_error.append(errorrate)

            print ("error rate for fold{}(LogisticRegression) with {}% train data is {}".format(i, train_rate, errorrate))

            j += 1

        mean_all_runs.append(np.mean(mean_per_run_error))

        print ("Mean error rate for fold{}(LogisticRegresson) is {}".format(i, np.mean(mean_per_run_error)))
        print ("Standard deviation for fold{}(LogisticRegression) is {}".format(i, np.std(mean_per_run_error)))

        i += 1

    print ("Mean error rate for all runs Logistic Regression is {}".format(np.mean(mean_all_runs)))
    print ("Standard deviation for all runs Logistic Regression is {}".format(np.std(mean_all_runs)))

flightPredictor()
