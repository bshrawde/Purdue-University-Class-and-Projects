	.text
.globl main
main:
# Save registers
	pushq %rbx
	pushq %r10
	pushq %r13
	pushq %r14
	pushq %r15
	subq $128,%rsp
	#Save arguments

	# push 8
	movq $8,%rbx

	# push 20
	movq $20,%r10

	# *
	imulq %r10,%rbx
     # func=malloc nargs=1
     # Move values from reg stack to reg args
	movq %rbx, %rdi
	call malloc
	movq %rax, %rbx
	#Assign to Local var a
	movq %rbx, 128(%rsp)

	# push 8
	movq $8,%rbx

	# push 9
	movq $9,%r10
	#Assign to Local var check heere 12 a
			 ### type 128
	movq %rbx, %rax
	imulq $8, %rax
	addq 128(%rsp), %rax
	movq %r10, (%rax)
	#top=0

	# push string "a[8]=%d\n" top=0
	movq $string0, %rbx

	# push 8
	movq $8,%r10
	#Assign2 to Local var a
			 ### type 8
	movq %r10, %rax
	imulq $8, %rax
	addq 128(%rsp), %rax
	movq (%rax), %r10
     # func=printf nargs=2
     # Move values from reg stack to reg args
	movq %r10, %rsi
	movq %rbx, %rdi
	movl    $0, %eax
	call printf
	movq %rax, %rbx
	addq $128,%rsp
# Restore registers
	popq %r15
	popq %r14
	popq %r13
	popq %r10
	popq %rbx
	ret
string0:
	.string "a[8]=%d\n"

