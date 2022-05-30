import hudsun.model.*

def ROOT_PATH = '/root/kernel/'
def gerrit_project = "${env.GERRIT_PROJECT}"
if(gerrit_project=='CJlinux-4.19.90'){

}

pipeline {
    agent {
        node {
            label 'Beijing-x86_64-node1'
            customWorkspace ROOT_PATH + gerrit_project
        }
    }
    stages {
        stage('Update'){
            steps {
                echo 'pipeline1'
                echo "Update kernel repository......"
                //sh 'cd ' + ROOT_PATH + '/Kernel-rpmbuild && git pull || true'
                //sh 'cd ' + ROOT_PATH + '/kabi-check && git pull || true'
                //sh 'git pull && git fetch ssh://chengjieos@$GERRIT_HOST:29418/$GERRIT_PROJECT $GERRIT_REFSPEC:temp && git checkout temp'
            }
        }
        stage('x86-check') {
            steps {
                echo 'build test......'
                //sh 'cp arch/x86/configs/openeuler_defconfig .config && make olddefconfig -j && make prepare -j && make scripts -j && make init -j && make drivers/scsi/scsi_sysfs.o && ../kabi-check/check_kabi.sh ../Kernel-rpmbuild/rpmbuild-4.19/SOURCES/Module.kabi_x86_64 ../kabi-check/Kabi.path_x86_64'
            }
        }
    }
    post {
        always {
            echo 'This will always run'
            //sh 'make distclean -j && git checkout linux-next && git branch -D temp'
        }
        success {
            echo 'This will run only if successful'
            //sh 'ssh -p 29418 jenkins@$GERRIT_HOST gerrit review --label verified=+1 --project $GERRIT_PROJECT $GERRIT_CHANGE_NUMBER,$GERRIT_PATCHSET_NUMBER'
            echo "${env.JOB_NAME}"
            //sh 'curl \'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=743a0a92-03b1-4a66-8df7-13c6a7665fc3\' -H \'Content-Type: application/json\' -d \'{ "msgtype": "markdown","markdown": {"content": "KABI校验结果：<font color=\\"info\\">成功</font>\\n 项目名称:<font color=\\"comment\\">\'$GERRIT_PROJECT\'</font>\\n 项目分支:<font color=\\"comment\\">\'$GERRIT_BRANCH\'</font>\\n 提交者:<font color=\\"comment\\">\'$GERRIT_PATCHSET_UPLOADER_EMAIL\'</font> \\n 修改链接:<font color=\\"comment\\">[\'$GERRIT_CHANGE_URL\'](\'$GERRIT_CHANGE_URL\')</font> \\n 构建链接:<font color=\\"comment\\">[\'$BUILD_URL\'](\'$BUILD_URL\')</font>"}}\''
        }
        failure {
            echo 'This will run only if failed'
            //sh 'ssh -p 29418 jenkins@$GERRIT_HOST gerrit review --code-review -1 --project $GERRIT_PROJECT $GERRIT_CHANGE_NUMBER,$GERRIT_PATCHSET_NUMBER'
            //sh 'curl \'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=0c40858f-d298-4c8d-843e-70fe5ed56e5f\' -H \'Content-Type: application/json\' -d \'{ "msgtype": "markdown","markdown": {"content": "KABI校验结果：<font color=\\"warning\\">失败</font>\\n 项目名称:<font color=\\"comment\\">\'$GERRIT_PROJECT\'</font>\\n 项目分支:<font color=\\"comment\\">\'$GERRIT_BRANCH\'</font>\\n 提交者:<font color=\\"comment\\">\'$GERRIT_PATCHSET_UPLOADER_EMAIL\'</font> \\n 修改链接:<font color=\\"comment\\">[\'$GERRIT_CHANGE_URL\'](\'$GERRIT_CHANGE_URL\')</font> \\n 构建链接:<font color=\\"comment\\">[\'$BUILD_URL\'](\'$BUILD_URL\')</font>"}}\''
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
    agent {
        node {
            label 'Beijing-aarch64-node2'
            customWorkspace ROOT_PATH + gerrit_project
        }
    }
    stages {
        stage('Update'){
            steps {
                echo 'pipeline2'
                echo "Update kernel repository......"
                //sh 'cd ' + ROOT_PATH + '/Kernel-rpmbuild && git pull || true'
                //sh 'cd ' + ROOT_PATH + '/kabi-check && git pull || true'
                //sh 'git pull && git fetch ssh://chengjieos@$GERRIT_HOST:29418/$GERRIT_PROJECT $GERRIT_REFSPEC:temp && git checkout temp'
            }
        }
        stage('x86-check') {
            steps {
                echo 'build test......'
                //sh 'cp arch/x86/configs/openeuler_defconfig .config && make olddefconfig -j && make prepare -j && make scripts -j && make init -j && make drivers/scsi/scsi_sysfs.o && ../kabi-check/check_kabi.sh ../Kernel-rpmbuild/rpmbuild-4.19/SOURCES/Module.kabi_x86_64 ../kabi-check/Kabi.path_x86_64'
            }
        }
    }
}

