import hudsun.model.*

def root_path = "/root/tmp-kernel/"

def gerrit_project="${env.GERRIT_PROJECT}".split("@")[0]
def mytest="test@2".split("@")[0]

def defconfig="openeuler_defconfig"
def defkabifile_x86_64="rpmbuild-4.19/SOURCES/Module.kabi_x86_64"
def defkabifile_aarch64="rpmbuild-4.19/SOURCES/Module.kabi_aarch64"

def kernel_rpmbuild_branch="master" //更多操作

def kernel_rpmbuild_path=root_path+ "Kernel-rpmbuild"
def kabi_check_path= root_path + "kabi-check"

def pulltrue=" && git pull || true "

if(gerrit_project=="CJlinux-4.19.90"){  //a版
    //不做处理
}
else if(gerrit_project=="uos/kernel"){  //e版
    defkabifile_x86_64="rpmbuild-4.19/Module.kabi_x86_64"
    defkabifile_aarch64="rpmbuild-4.19/Module.kabi_aarch64"
    kernel_rpmbuild_branch="105xe"
}else if(gerrit_project=="UOS-kernel-D-4.19-arm"){  //test

}


pipeline {
    agent none
    stages {
        stage("x86_64-check"){
            agent{
                node{
                    label "Beijing-x86_64-node1"
                    customWorkspace root_path + "UOS-kernel-D-4.19-arm"
                }
            }
            steps {
                echo "x86_64 check..."+gerrit_project
                echo "Update repo..."+mytest
                sh "cd " + kernel_rpmbuild_path + " && git checkout " + kernel_rpmbuild_branch + pulltrue
                sh "cd " + kabi_check_path + pulltrue
                sh "git checkout $GERRIT_BRANCH " + pulltrue
                sh "git fetch ssh://chengjieos@$GERRIT_HOST:29418/$GERRIT_PROJECT $GERRIT_REFSPEC:temp && git checkout temp"
                echo "Start kabi check..."
                sh "cp arch/x86/configs/" + defconfig + " .config && make olddefconfig -j"
                sh "make prepare -j && make scripts -j && make init -j"
                sh "make drivers/scsi/scsi_sysfs.o"
                //sh kabi_check_path+"/check_kabi.sh "+kabi_check_path+"/Kabi.path_x86_64 "+kernel_rpmbuild_path+"/"+defkabifile_x86_64
            }
        }
        stage("arm64-check") {
            agent{
                node{
                    label "Beijing-aarch64-node2"
                    customWorkspace root_path + gerrit_project
                }
            }
            steps {
                echo "arm64 check..."+gerrit_project
                sh "lscpu"
            }
        }
    }
    
}

