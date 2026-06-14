#include<stdio.h>
#include<conio.h>
#include<string.h> 

typedef struct{
    char name[100];
    int acc_no;
    float balance;
}ACCOUNT;

const char* ACCOUNT_NAME ="acc.dat";

void create_account();
void deposit_money();
void withdraw_money();
int check_balance();
int input_acc_no();
float input_balance();
void fix_fgets(char *s);
float input_amount() ;
int main()
{
    while(1){
        int choice;
        printf("\n==========BANK MANAGEMENT SYSTEM===========");
        printf("\n1. Create Account");
        printf("\n2. Deposit Money");
        printf("\n3. Withdraw Money");
        printf("\n4. Check Balance");
        printf("\n5. Exit");
        printf("\nEnter your choice : ");
        //error handling 
        char extra;
        if(scanf("%d%c",&choice,&extra) != 2 || extra !='\n'){
            printf("Invalid input! Please enter a valid number(1-5).\n");
            while(getchar() != '\n'); //clear buffer
            continue;
        }
        switch(choice){
        case 1:
            create_account();
            break;
        case 2:
            deposit_money();
            break;
        case 3:
            withdraw_money();
            break;
        case 4:
            check_balance();
            break;
        case 5:
            printf("\nClosing the bank, Thanks for your visit.");
            return 0;
            break;
        default:
            printf("Invalid input! Please enter a valid number(1-5).\n");
            break;
        }
    }
    return 0;
}
void create_account()
{
    FILE *acc_file = fopen(ACCOUNT_NAME,"ab+"); //append binary,we are supposed to append files at the end of the  file
    if(acc_file == NULL){
        printf("Enable to open file");
        return ;
    }
    ACCOUNT acc;
    printf("\nCreating Account");
    printf("\nEnter your name : ");
    getchar();  //cler newline left in buffer
    fgets(acc.name,sizeof(acc.name),stdin);
    fix_fgets(acc.name);
    acc.acc_no = input_acc_no();
    if (acc.acc_no == 0) {
        fclose(acc_file);
        return;
    }
    acc.balance = 0;
    fwrite(&acc,sizeof(acc),1,acc_file);
    fclose(acc_file);
    printf("Account created successfully.\n");

}
int check_balance()
{
    printf("\nChecking Balance");
    FILE* acc_file = fopen(ACCOUNT_NAME,"rb");
    if(acc_file == NULL){
        printf("\n unable to open file");
        return 1;
    }
     int acc_no = input_acc_no();
    if (acc_no == 0) {
        fclose(acc_file);
        return 1;
    }
    ACCOUNT acc_read;
    while(fread(&acc_read,sizeof(acc_read),1,acc_file)){
        if(acc_read.acc_no == acc_no){
            printf("Your current balance is Rs: %.02f",acc_read.balance);
            fclose(acc_file);
            return 0;
        }
    }
    printf("\nAccount no %d wasn't found",acc_no);
    fclose(acc_file);
    return 1;
}
int input_acc_no()
{
    int acc_no;
    printf("\nEnter your Account Number : ");
    char extra;
    if(scanf("%d%c",&acc_no,&extra) != 2 || extra !='\n'){
        printf("Invalid account number!!!Please enter a valid account number.\n");
        while(getchar() != '\n');
        return 0;
    }
    if(acc_no <= 0){
    	printf("Account number must be positive.\n");
        return 0;
    }
    return acc_no;
}
float input_balance()
{
    float money;
    char extra;
    if(scanf("%f%c",&money,&extra) != 2 || extra !='\n'){
        printf("Amount invalid! Please enter a valid amount\n");
        while(getchar() != '\n');
        return 0;
    }
    if(money <= 0){
    	printf("Amount must be greater than zero.\n");
        return 0;
    }
    return money;
}
void fix_fgets(char * s)
{
    s[strcspn(s,"\n")] = '\0';
}
void deposit_money()
{
    FILE *acc_file=fopen(ACCOUNT_NAME,"rb+");  //we have to read then overwrite the balance
    if(acc_file==NULL){
        printf("\nUnable to open Account's File!\n");
        return;
    }

    ACCOUNT acc_deposit; //fread will put the data here

    // Account Number Input with Validation
    int acc_no;
    do{
        acc_no=input_acc_no();
        if(acc_no==0){
            continue;
        }
        break;
    }while(1);

    // Amount Input with Validation
    float money;
        do{
            printf("Enter amount of money to be deposited: ");
            money=input_amount();  //amt to be deposited
            if(money==0){
                continue;
            }
            break;
    }while(1);

    while(fread(&acc_deposit,sizeof(acc_deposit),1,acc_file)){  //read the file untill acc. no. matches and stores the structure one by one in acc_deposit replacing the previous as count is 1
        if(acc_deposit.acc_no==acc_no){  //cursor reaches at the end of this structure after reading this structure
            acc_deposit.balance += money; //adding money
            fseek(acc_file,-(long)sizeof(acc_deposit),SEEK_CUR);  //moving cursor to the start of this structure
            fwrite(&acc_deposit,sizeof(acc_deposit),1,acc_file);  //only overwrites this structure as count is 1
            fclose(acc_file);
            printf("\nSuccessfully deposited RS.%.2f New balance is Rs.%.2f\n",money,acc_deposit.balance);
            return;
        }
    }
    fclose(acc_file);
    printf("\nAccount no. %d not found in the records.\nPlease try again\n",acc_no);
}
void withdraw_money()
{
    FILE *acc_file=fopen(ACCOUNT_NAME,"rb+");
    if(acc_file==NULL){
        printf("\nUnable to open Account's File!");
        return;
    }

    ACCOUNT acc_withdraw;

    // Account Number Input with Validation
    int acc_no;
    do{
        acc_no=input_acc_no();
        if(acc_no==0){
            continue;
        }
        break;
    }while(1);

    // Amount Input with Validation
    float money;
        do{
            printf("Enter amount of money to be withdrawn: ");
            money=input_amount();  //amt to be deposited
            if(money==0){
                continue;
            }
            break;
    }while(1);

    while(fread(&acc_withdraw,sizeof(acc_withdraw),1,acc_file)){
        if(acc_withdraw.acc_no==acc_no){

            if(money>acc_withdraw.balance){
                printf("Not enough balance. Current balance: %.2f\n",acc_withdraw.balance);
            }
            else{
                acc_withdraw.balance -= money;
                fseek(acc_file,-(long)sizeof(acc_withdraw),SEEK_CUR);
                fwrite(&acc_withdraw,sizeof(acc_withdraw),1,acc_file);
                fclose(acc_file);
                printf("\nSuccessfully withdrawn RS.%.2f New balance is Rs.%.2f\n",money,acc_withdraw.balance);
            }
        fclose(acc_file);
        return;
        }
    }
    fclose(acc_file);
    printf("\nAccount no. %d not found in the records.\nPlease try again\n",acc_no);
}
float input_amount()    // definition
{
    float money;
    char extra;
    if(scanf("%f%c",&money,&extra) != 2 || extra !='\n'){
        printf("Amount invalid! Please enter a valid amount\n");
        while(getchar() != '\n');
        return 0;
    }
    if(money <= 0){
        printf("Amount must be greater than zero.\n");
        return 0;
    }
    return money;
}
